package ru.droidwelt.prototype8.utils.common;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    DatabaseHelper provideDatabaseHelper() {
        return new DatabaseHelper( );
    }
}
