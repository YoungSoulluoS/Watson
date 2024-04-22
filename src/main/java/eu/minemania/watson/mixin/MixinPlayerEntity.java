package eu.minemania.watson.mixin;

import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.authlib.GameProfile;
import eu.minemania.watson.chat.Highlight;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity
{
    @Shadow
    public abstract Text getDisplayName();

    protected MixinPlayerEntity(World world, GameProfile gameprofile)
    {
        super(EntityType.PLAYER, world);
    }

    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getScoreboardTeam()Lnet/minecraft/scoreboard/Team;"))
    private Team getCustomScoreboardTeam(PlayerEntity player)
    {
        if (Highlight.changeUsername)
        {
            return null;
        }
        else
        {
            return player.getScoreboardTeam();
        }
    }

    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getName()Lnet/minecraft/text/Text;"))
    private Text getCustomUsername(PlayerEntity player)
    {
        if (Highlight.changeUsername)
        {
            return Text.literal(Highlight.getUsername());
        }
        else
        {
            return player.getName();
        }
    }

    @ModifyVariable(method = "addTellClickEvent(Lnet/minecraft/text/MutableText;)Lnet/minecraft/text/MutableText;", at = @At("HEAD"))
    private MutableText changeUsernameColor(MutableText componentln)
    {
        if (Highlight.changeUsername && Highlight.getStyle() != null)
        {
            componentln.setStyle(Highlight.getStyle());
        }
        return componentln;
    }

    @Redirect(method = "addTellClickEvent(Lnet/minecraft/text/MutableText;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/GameProfile;getName()Ljava/lang/String;"))
    private String changeCustomUsername(GameProfile gameProfile)
    {
        String username = gameProfile.getName();
        if (Highlight.changeUsername)
        {
            username = Highlight.getUsername();
        }
        return username;
    }
}