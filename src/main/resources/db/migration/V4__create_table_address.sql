CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(100) NOT NULL,
    complement VARCHAR(255),
    cep VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índice para melhorar a busca por usuário
CREATE INDEX IF NOT EXISTS idx_address_user_id ON address(user_id);

-- Índice para melhorar buscas por CEP (opcional, útil para filtros/consultas)
CREATE INDEX IF NOT EXISTS idx_address_cep ON address(cep);
