CREATE TABLE customers
(
    dni varchar(20) NOT NULL,
    full_name varchar(50) NOT NULL,
    credit_card varchar(20) NOT NULL,
    total_flights int NOT NULL,
    total_lodgings int NOT NULL,
    total_tours int NOT NULL,
    phone_number varchar(20) NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY(dni)
);

CREATE TABLE flights
(
    id bigserial NOT NULL,
    origin_lat decimal NOT NULL,
    origin_lng decimal NOT NULL,
    destiny_lng decimal NOT NULL,
    destiny_lat decimal NOT NULL,
    origin_name varchar(20) NOT NULL,
    destiny_name varchar(20) NOT NULL,
    aero_line varchar(20) NOT NULL,
    price decimal NOT NULL,
    CONSTRAINT pk_fly PRIMARY KEY(id)
);

CREATE TABLE hotels
(
    id bigserial NOT NULL,
    name varchar(50) NOT NULL,
    address varchar(50) NOT NULL,
    rating int NOT NULL,
    price decimal NOT NULL,
    CONSTRAINT pk_hotel PRIMARY KEY(id)
);

CREATE TABLE tours
(
    id bigserial NOT NULL,
    customer_id varchar(20) NOT NULL,
    CONSTRAINT pk_tour PRIMARY KEY(id),
    CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES customers(dni) ON DELETE NO ACTION
);

CREATE TABLE reservations
(
    id uuid NOT NULL,
    date_reservation timestamp NOT NULL,
    date_start date NOT NULL,
    date_end date NULL,
    total_days int NOT NULL,
    price decimal not null,
    tour_id bigint,
    hotel_id bigint NOT NULL,
    customer_id varchar(20) NOT NULL,
    CONSTRAINT pk_reservation PRIMARY KEY(id),
    -- Si elimino un cliente, se eliminarán automáticamente sus reservas asociadas
    CONSTRAINT fk_customer_r FOREIGN KEY(customer_id) REFERENCES customers(dni) ON DELETE CASCADE,
    -- Si elimino un hotel, se eliminarán automáticamente sus reservas asociadas
    CONSTRAINT fk_hotel_r FOREIGN KEY(hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    -- Si elimino un tour, se eliminarán automáticamente sus reservas asociadas
    CONSTRAINT fk_tour_r FOREIGN KEY(tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

CREATE TABLE tickets
(
    id uuid NOT NULL,
    price decimal NOT NULL,
    flight_id bigint NOT NULL,
    customer_id varchar(20) NOT NULL,
    departure_date timestamp NOT NULL,
    arrival_date timestamp NOT NULL,
    purchase_date timestamp NOT NULL,
    tour_id bigint,
    CONSTRAINT pk_ticket PRIMARY KEY(id),
    -- Si elimino un cliente, se eliminarán automáticamente sus tickets asociados
    CONSTRAINT fk_customer_t FOREIGN KEY(customer_id) REFERENCES customers(dni) ON DELETE CASCADE,
    -- Si elimino un vuelo, se eliminarán automáticamente sus tickets asociados
    CONSTRAINT fk_flight_t FOREIGN KEY(flight_id) REFERENCES flights(id) ON DELETE CASCADE,
    -- Si elimino un tour, se eliminarán automáticamente sus tickets asociados
    CONSTRAINT fk_tour_t FOREIGN KEY(tour_id) REFERENCES tours(id) ON DELETE CASCADE
);
