package com.chain.autostoragesystem.block.custom;

import com.chain.autostoragesystem.block.custom.base.AbstractBlockWithEntity;
import com.chain.autostoragesystem.entity.custom.ImportBusEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


/**
 * Шина импорта предметов В систему
 */
public class ImportBusBlock extends AbstractBlockWithEntity {

    public ImportBusBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ImportBusEntity(pPos, pState);
    }
}
