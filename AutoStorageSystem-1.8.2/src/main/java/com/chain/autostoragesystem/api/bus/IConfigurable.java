package com.chain.autostoragesystem.api.bus;

import com.chain.autostoragesystem.api.storage_system.Config;

import javax.annotation.Nonnull;

public interface IConfigurable {
    void setConfig(@Nonnull Config config);
}
