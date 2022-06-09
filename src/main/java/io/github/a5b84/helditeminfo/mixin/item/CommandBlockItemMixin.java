package io.github.a5b84.helditeminfo.mixin.item;

import io.github.a5b84.helditeminfo.TooltipAppender;
import io.github.a5b84.helditeminfo.HeldItemInfo;
import io.github.a5b84.helditeminfo.TooltipBuilder;
import io.github.a5b84.helditeminfo.Util;
import net.minecraft.item.CommandBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

import static io.github.a5b84.helditeminfo.HeldItemInfo.config;

@Mixin(CommandBlockItem.class)
public abstract class CommandBlockItemMixin implements TooltipAppender {

    @Override
    public boolean heldItemInfo_shouldAppendTooltip() {
        return config.showCommandBlockInfo();
    }

    @Override
    public void heldItemInfo_appendTooltip(TooltipBuilder builder) {
        // Get the command
        NbtCompound tag = builder.stack.getSubTag("BlockEntityTag");
        if (tag == null) return;
        String command = tag.getString("Command");
        if (command == null) return;
        command = command.trim();
        if (command.isEmpty()) return;

        // Formatting
        int maxLines = Math.min(config.maxCommandLines(), builder.getRemainingLines());
        List<MutableText> lines = Util.wrapLines(command, maxLines);
        if (!lines.isEmpty()) {
            for (MutableText text : lines) {
                text.formatted(TooltipBuilder.DEFAULT_COLOR);
            }
            builder.appendAll(lines);
        }
    }

}
