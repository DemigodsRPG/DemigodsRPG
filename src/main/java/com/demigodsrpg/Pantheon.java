package com.demigodsrpg;

import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.family.Family;

public interface Pantheon {

    String getName();

    Family[] getFamilies();

    Deity[] getDeities();
}
