--
--    Copyright (C) 2021 RadixIot Inc. All rights reserved.
--    @author Terry Packer
--
CREATE TABLE simpleEntity
(
    id               INT          NOT NULL AUTO_INCREMENT,
    xid              VARCHAR(100) NOT NULL,
    enabled          CHAR(1),
    name             VARCHAR(255),
    PRIMARY KEY (id)
);
ALTER TABLE simpleEntity
    ADD CONSTRAINT simpleEntityUn1 UNIQUE (xid);
