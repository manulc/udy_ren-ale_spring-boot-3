
db.createCollection('users', { capped: false });

db.users.insert([
    {
        "username": "ragnar777",
        "dni": "VIKI771012HMCRG093",
        "enabled": true,
        "password": "$2a$10$PqIy0bWgbd83DWtQJvK5t.4HkMRYHzeDHBV0Z3dZ46GtdSW7NmYxe", // "s3cr3t"
        "roles": ["ROLE_USER"]
    },
    {
        "username": "heisenberg",
        "dni": "BBMB771012HMCRR022",
        "enabled": true,
        "password": "$2a$10$4qJIFPkuXJ3AN5nayr7FXe3a.rvlnO9CYJL44evo5/SUzHHPLy5pK", // "p4sw0rd"
        "roles": ["ROLE_USER"]
    },
    {
        "username": "misterX",
        "dni": "GOTW771012HMRGR087",
        "enabled": true,
        "password": "$2a$10$qoqkwGSmdBXsCxcWY9mJ1eEk2ccEe8eQbKTaM0qpCwQ9/ijIF2u8.", // "misterX123"
        "roles": ["ROLE_USER", "ROLE_ADMIN"]

    },
    {
        "username": "neverMore",
        "dni": "WALA771012HCRGR054",
        "enabled": true,
        "password": "$2a$10$jvHVSxgci8pU/jm4Zjt18OznQ0CwSSBBmglrl4JF2uF0YG9HU9usi", // "4dmIn"
        "roles": ["ROLE_ADMIN"]
    }
]);