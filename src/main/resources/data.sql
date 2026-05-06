-- Seed data for Library Management System
-- This file runs after Hibernate creates/updates the schema

-- Insert categories (only if the table is empty)
INSERT INTO categories (name, description, is_deleted)
SELECT 'Fiction', 'Fictional works including novels and short stories', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Fiction');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Non-Fiction', 'Factual books including biographies, history, and science', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Non-Fiction');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Science', 'Scientific literature and textbooks', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Science');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Technology', 'Books about computers, programming, and technology', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Technology');

INSERT INTO categories (name, description, is_deleted)
SELECT 'History', 'Historical literature and reference books', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'History');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Literature', 'Classic and modern literary works', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Literature');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Philosophy', 'Philosophical works and thought-provoking literature', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Philosophy');

INSERT INTO categories (name, description, is_deleted)
SELECT 'Art', 'Books about art, design, and visual culture', false
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Art');
