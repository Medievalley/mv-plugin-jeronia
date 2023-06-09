package org.shrigorevich.ml.state.structures;

class VolumeQueryBuilderImpl {

    String create(String name, int sizeX, int sizeY, int sizeZ) {
        return String.format(
            "INSERT INTO volume (name, size_x, size_y, size_z) VALUES ('%s', %d, %d, %d) returning id;",
            name, sizeX, sizeY, sizeZ
        );
    }

    String createVolumeBlockBatch() {
        return "INSERT INTO volume_block (volume_id, material, block_data, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
    }

    String getBlocks(int volumeId) {
        return String.format("SELECT id, material, block_data as blockdata, x, y, z FROM volume_block where volume_id=%d;", volumeId);
    }
}
