TRUNCATE TABLE Users;

INSERT INTO Users ("id", "email", "password", "roles") VALUES
-- admin@test.com password1
 ('91625c14-cf09-43d6-9559-1a78636f3ab3','admin@test.com', 'WbB1NmsbfPWv2Vngh8BeudH40ftvxr0/ezxE3dH4REI=', '["ADMIN", "USER"]'),
-- user@test.com password2
 ('d3f8b7a1-42c9-4e65-8a2b-7c4f1e9d2b10','user@test.com', '1B6jOD26MRJn+0WhFE3MwlMzW5fdq0FOkHANhfbT1Xg=', '["USER"]')
 ;
