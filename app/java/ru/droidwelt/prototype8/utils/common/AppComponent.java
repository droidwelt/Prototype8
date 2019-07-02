package ru.droidwelt.prototype8.utils.common;

import dagger.Component;
import ru.droidwelt.prototype8.main.MainActivity;

@Component(modules = {StorageModule.class})

/*
public interface AppComponent {
    DatabaseHelper getDatabaseHelper();
}
*/



public interface AppComponent {

    void inject(MainActivity activity);
}


