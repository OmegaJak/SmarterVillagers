package omegajak.smartervillagers.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.village.Merchant;
import omegajak.smartervillagers.api.MerchantProvider;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.ai.brain.SmarterVillagerActivities;
import omegajak.smartervillagers.ai.brain.SmarterVillagerMemoryModules;

public class Networking {
    private static final Identifier FOLLOW_PACKET = SmarterVillagers.id("follow_packet");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(FOLLOW_PACKET, Networking::receiveFollowPacket);
    }

    @Environment(EnvType.CLIENT)
    public static void sendFollowPacket() {
        ClientPlayNetworking.send(Networking.FOLLOW_PACKET, new PacketByteBuf(Unpooled.buffer()));
        System.out.println("Sending follow packet from client");
    }

    private static void receiveFollowPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ScreenHandler screenHandler = player.currentScreenHandler;
        if (screenHandler instanceof MerchantProvider) {
            MerchantProvider merchantProvider = (MerchantProvider)screenHandler;
            Merchant merchant = merchantProvider.getMerchant();
            if (merchant instanceof VillagerEntity) {
                VillagerEntity villagerEntity = (VillagerEntity)merchant;
                villagerEntity.getBrain().remember(SmarterVillagerMemoryModules.LEADER, player);
                villagerEntity.getBrain().remember(SmarterVillagerMemoryModules.STARTED_FOLLOWING_TIME, player.world.getTime());
                villagerEntity.getBrain().doExclusively(SmarterVillagerActivities.FOLLOW_LEADER_ACTIVITY);

                player.closeHandledScreen();
            }
        }
    }
}
