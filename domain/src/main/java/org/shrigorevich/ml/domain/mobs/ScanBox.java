package org.shrigorevich.ml.domain.mobs;

import org.shrigorevich.ml.common.Coords;

public interface ScanBox {
    Coords getMin();
    Coords getMax();
}
