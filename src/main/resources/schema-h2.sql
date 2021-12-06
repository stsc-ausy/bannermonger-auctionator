DROP TABLE IF EXISTS banner_spec_topic;
DROP TABLE IF EXISTS banner_quote_request;
DROP TABLE IF EXISTS banner_offer;
DROP TABLE IF EXISTS banner_order;
DROP TABLE IF EXISTS banner_spec;

CREATE TABLE banner_spec (
  object_id UUID PRIMARY KEY NOT NULL,
  reference VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  banner_size VARCHAR(20) NOT NULL,
  relevance_required DOUBLE NOT NULL,
  max_price_euro DOUBLE NOT NULL,
  min_page_views BIGINT NOT NULL,
  valid_through TIMESTAMP NOT NULL,
  target_gender VARCHAR(10) NOT NULL,
  target_age_group VARCHAR(20) NOT NULL
);

CREATE TABLE banner_spec_topic (
  banner_spec_id UUID NOT NULL,
  topic VARCHAR(50) NOT NULL,
  CONSTRAINT PK_TOPIC PRIMARY KEY (banner_spec_id, topic),
  CONSTRAINT FK_BANNER_SPEC_TOPIC FOREIGN KEY (banner_spec_id) REFERENCES banner_spec(object_id)
);

CREATE TABLE banner_quote_request (
  object_id UUID PRIMARY KEY NOT NULL,
  reference VARCHAR(255) NOT NULL,
  image_width INT NOT NULL,
  image_height INT NOT NULL,
  min_page_views BIGINT NOT NULL,
  gender VARCHAR(10) NOT NULL,
  min_age INT NOT NULL,
  max_age INT NOT NULL,
  tags VARCHAR(255) NOT NULL,
  deadline TIMESTAMP NOT NULL,
  banner_spec_id UUID NOT NULL,
  CONSTRAINT FK_BANNER_QR_SPEC FOREIGN KEY (banner_spec_id) REFERENCES banner_spec(object_id)
);

CREATE TABLE banner_offer (
  object_id UUID PRIMARY KEY NOT NULL,
  offer_id VARCHAR(255) NOT NULL,
  reference VARCHAR(255) NOT NULL,
  issuer VARCHAR(255) NOT NULL,
  price_model VARCHAR(10) NOT NULL,
  guaranteed_relevance DOUBLE NOT NULL,
  guaranteed_page_views BIGINT NOT NULL,
  price_in_euro DOUBLE NOT NULL,
  offer_validity TIMESTAMP NOT NULL,
  banner_spec_id UUID NOT NULL,
  CONSTRAINT FK_BANNER_OFFER_SPEC FOREIGN KEY (banner_spec_id) REFERENCES banner_spec(object_id)
);

CREATE TABLE banner_order (
  object_id UUID PRIMARY KEY NOT NULL,
  offer_id VARCHAR(255) NOT NULL,
  reference VARCHAR(255) NOT NULL,
  provider VARCHAR(255) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  ordered_at TIMESTAMP NOT NULL,
  banner_spec_id UUID NOT NULL,
  CONSTRAINT FK_BANNER_ORDER_SPEC FOREIGN KEY (banner_spec_id) REFERENCES banner_spec(object_id)
);
