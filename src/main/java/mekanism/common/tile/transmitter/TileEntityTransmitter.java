package mekanism.common.tile.transmitter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.Coord4D;
import mekanism.api.IAlloyInteraction;
import mekanism.api.transmitters.DynamicNetwork;
import mekanism.api.transmitters.DynamicNetwork.NetworkClientRequest;
import mekanism.api.transmitters.IGridTransmitter;
import mekanism.api.transmitters.TransmitterNetworkRegistry;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.transmitters.TransmitterImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileEntityTransmitter<A, N extends DynamicNetwork<A, N, BUFFER>, BUFFER> extends
      TileEntitySidedPipe implements IAlloyInteraction {

    public TransmitterImpl<A, N, BUFFER> transmitterDelegate;

    public boolean unloaded = true;
    public boolean dataRequest = false;
    public boolean delayedRefresh = false;

    private N lastClientNetwork = null;

    public TileEntityTransmitter() {
        transmitterDelegate = new TransmitterImpl<>(this);
    }

    public TransmitterImpl<A, N, BUFFER> getTransmitter() {
        return transmitterDelegate;
    }

    public abstract N createNewNetwork();

    public abstract N createNetworkByMerging(Collection<N> networks);

    @Override
    public void onWorldJoin() {
        if (!getWorld().isRemote) {
            TransmitterNetworkRegistry.registerOrphanTransmitter(getTransmitter());
        } else if (lastClientNetwork != null) {
            getTransmitter().setTransmitterNetwork(lastClientNetwork);
        }

        unloaded = false;
    }

    @Override
    public void update() {
        super.update();

        if (delayedRefresh) {
            //Gets run the tick after the variable has been set. This is enough
            // time to ensure that the transmitter has been registered.
            delayedRefresh = false;
            refreshConnections();
        }

        if (getWorld().isRemote) {
            if (!dataRequest) {
                dataRequest = true;
                MinecraftForge.EVENT_BUS.post(new NetworkClientRequest(getWorld().getTileEntity(getPos())));
            }
        }
    }

    @Override
    public void onChunkUnload() {
        if (!getWorld().isRemote) {
            getTransmitter().takeShare();
        }

        super.onChunkUnload();
    }

    @Override
    public void onWorldSeparate() {
        unloaded = true;

        if (!getWorld().isRemote) {
            TransmitterNetworkRegistry.invalidateTransmitter(getTransmitter());
        } else {
            lastClientNetwork = getTransmitter().getTransmitterNetwork();
            getTransmitter().setTransmitterNetwork(null);
        }
    }

    @Override
    public void markDirtyTransmitters() {
        super.markDirtyTransmitters();

        if (getTransmitter().hasTransmitterNetwork()) {
            TransmitterNetworkRegistry.invalidateTransmitter(getTransmitter());
        }
    }

    @Override
    public void markDirtyAcceptor(EnumFacing side) {
        super.markDirtyAcceptor(side);

        if (getTransmitter().hasTransmitterNetwork()) {
            getTransmitter().getTransmitterNetwork().acceptorChanged(getTransmitter(), side);
        }
    }

    protected boolean canHaveIncompatibleNetworks() {
        return false;
    }

    @Override
    protected void recheckConnections(byte newlyEnabledTransmitters) {
        if (canHaveIncompatibleNetworks() && getTransmitter().hasTransmitterNetwork()) {
            //We only need to check if we can have incompatible networks and if we actually have a network
            for (EnumFacing side : EnumFacing.values()) {
                if (connectionMapContainsSide(newlyEnabledTransmitters, side)) {
                    //Recheck the side that is now enabled, as we manually merge this
                    // cannot be simplified to a first match is good enough
                    recheckConnectionPrechecked(side);
                }
            }
        }
    }

    @Override
    protected void recheckConnection(EnumFacing side) {
        if (canHaveIncompatibleNetworks() && getTransmitter().hasTransmitterNetwork()) {
            //We only need to check if we can have incompatible networks and if we actually have a network
            recheckConnectionPrechecked(side);
        }
    }

    private void recheckConnectionPrechecked(EnumFacing side) {
        N network = getTransmitter().getTransmitterNetwork();
        TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
        if (tileEntity instanceof TileEntityTransmitter) {
            TileEntityTransmitter other = (TileEntityTransmitter) tileEntity;
            //The other one should always have the same incompatible networks state as us
            // But just in case it doesn't just check the boolean
            if (other.canHaveIncompatibleNetworks() && other.getTransmitter().hasTransmitterNetwork()) {
                N otherNetwork = (N) other.getTransmitter().getTransmitterNetwork();
                if (network != otherNetwork && network.isCompatibleWith(otherNetwork)) {
                    //We have two networks that are now compatible and they are not the same source network
                    // The most common cause they would be same source network is that they would merge
                    // from the first pipe checking when it attempts to reconnect, and then the second
                    // pipe still is going to be checking the connection.
                    //
                    // We continue to return false for this case so that it is able to check if there
                    // are more sides that pipe can now connect to where it previously could not
                    //
                    // Manually merge the networks.
                    // This code is not in network registry as there is special handling needed to ensure
                    // it visually updates properly. There also were above checks that get us to a certain
                    // point where we can make some assumptions about the networks and if it is actually
                    // valid to merge them when otherwise people may try to merge things when they shouldn't
                    // be merged causing unexpected bugs.
                    network.adoptTransmittersAndAcceptorsFrom(otherNetwork);
                    //Unregister the other network
                    otherNetwork.deregister();
                    //Commit the changes of the new network
                    network.commit();

                    //TODO: Update the visuals of the network
                }
            }
        }
    }

    public abstract A getCachedAcceptor(EnumFacing side);

    protected TileEntity getCachedTile(EnumFacing side) {
        ConnectionType type = connectionTypes[side.ordinal()];

        if (type == ConnectionType.PULL || type == ConnectionType.NONE) {
            return null;
        }

        return connectionMapContainsSide(currentAcceptorConnections, side) ? cachedAcceptors[side.ordinal()] : null;
    }

    @Override
    public void onAlloyInteraction(EntityPlayer player, EnumHand hand, ItemStack stack, int tierOrdinal) {
        if (getTransmitter().hasTransmitterNetwork()) {
            int upgraded = 0;
            Object[] array = ((LinkedHashSet) getTransmitter().getTransmitterNetwork().transmitters.clone()).toArray();

            Arrays.sort(array, (o1, o2) ->
            {
                if (o1 instanceof IGridTransmitter && o2 instanceof IGridTransmitter) {
                    Coord4D thisCoord = new Coord4D(getPos(), getWorld());

                    Coord4D o1Coord = ((IGridTransmitter) o1).coord();
                    Coord4D o2Coord = ((IGridTransmitter) o2).coord();

                    return Integer.compare(o1Coord.distanceTo(thisCoord), o2Coord.distanceTo(thisCoord));
                }

                return 0;
            });

            for (Object iter : array) {
                if (iter instanceof TransmitterImpl) {
                    TileEntityTransmitter t = ((TransmitterImpl) iter).containingTile;

                    if (t.upgrade(tierOrdinal)) {
                        upgraded++;

                        if (upgraded == 8) {
                            break;
                        }
                    }
                }
            }

            if (upgraded > 0) {
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);

                    if (stack.getCount() == 0) {
                        player.setHeldItem(hand, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    public boolean upgrade(int tierOrdinal) {
        return false;
    }

    public abstract int getCapacity();

    @Nullable
    public abstract BUFFER getBuffer();

    @Nullable
    public BUFFER getBufferWithFallback() {
        BUFFER buffer = getBuffer();
        //If we don't have a buffer try falling back to the network's buffer
        if (buffer == null && getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().getBuffer();
        }
        return buffer;
    }

    public abstract void takeShare();

    public abstract void updateShare();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return capability == Capabilities.GRID_TRANSMITTER_CAPABILITY
              || capability == Capabilities.ALLOY_INTERACTION_CAPABILITY
              || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (capability == Capabilities.GRID_TRANSMITTER_CAPABILITY) {
            return Capabilities.GRID_TRANSMITTER_CAPABILITY.cast(getTransmitter());
        } else if (capability == Capabilities.ALLOY_INTERACTION_CAPABILITY) {
            return Capabilities.ALLOY_INTERACTION_CAPABILITY.cast(this);
        }

        return super.getCapability(capability, side);
    }
}
