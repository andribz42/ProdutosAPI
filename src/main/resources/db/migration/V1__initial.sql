CREATE TABLE IF NOT EXISTS fornecedor (
	id UUID PRIMARY KEY,
	nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS produto (
	id UUID PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	preco FLOAT NOT NULL,
	quantidade INTEGER NOT NULL,
	fornecedor_id UUID NOT NULL,
	CONSTRAINT unique_nome_fornecedor UNIQUE (nome, fornecedor_id),
	CONSTRAINT fk_produto_fornecedor
        FOREIGN KEY (fornecedor_id) 
        REFERENCES fornecedor (id)
);