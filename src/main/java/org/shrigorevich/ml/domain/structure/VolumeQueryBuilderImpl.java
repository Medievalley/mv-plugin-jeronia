package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.volume.models.VolumeModel;

public class VolumeQueryBuilderImpl {

    String create(VolumeModel v) {
        return String.format(
                "INSERT INTO volume (name, size_x, size_y, size_z) VALUES ('%s', %d, %d, %d) returning id;",
                v.getName(), v.getSizeX(), v.getSizeY(), v.getSizeZ()
        );
    }

    String createVolumeBlockBatch() {
        return "INSERT INTO volume_block (volume_id, type, block_data, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
    }

    String getBlocks(int volumeId) {
        return String.format("SELECT id, type, block_data as blockdata, x, y, z FROM volume_block where volume_id=%d;", volumeId);
    }
}
