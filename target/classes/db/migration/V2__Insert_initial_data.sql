-- Clear existing data first
DELETE FROM items;
DELETE FROM categories;
DELETE FROM locations;
DELETE FROM branches;

-- Reset sequences
ALTER SEQUENCE categories_id_seq RESTART WITH 1;
ALTER SEQUENCE branches_id_seq RESTART WITH 1;
ALTER SEQUENCE locations_id_seq RESTART WITH 1;
ALTER SEQUENCE items_id_seq RESTART WITH 1;

-- Insert Categories with icon names
INSERT INTO categories (name, description, icon_name, created_at, updated_at) VALUES
('Breakfast', 'Delicious breakfast items to start your day', 'GiCroissant', NOW(), NOW()),
('Drinks', 'Refreshing beverages and hot drinks', 'FaCoffee', NOW(), NOW()),
('Soups', 'Warming and hearty soup dishes', 'LuSoup', NOW(), NOW()),
('Sushi', 'Fresh and authentic sushi selections', 'GiSushis', NOW(), NOW());

-- Insert Branches
INSERT INTO branches (name, phone, description, opening_hours, is_active, created_at, updated_at) VALUES
('Cheko Downtown', '+966-11-123-4567', 'Our flagship location in downtown Riyadh', '9:00 AM - 11:00 PM', true, NOW(), NOW()),
('Cheko Mall Branch', '+966-11-234-5678', 'Located in the heart of the shopping district', '10:00 AM - 12:00 AM', true, NOW(), NOW()),
('Cheko North Branch', '+966-11-345-6789', 'Serving the northern districts of Riyadh', '9:00 AM - 11:00 PM', true, NOW(), NOW());

-- Insert Locations
INSERT INTO locations (branch_id, address, latitude, longitude, city, state, country, created_at, updated_at) VALUES
(1, 'King Fahd Road, Downtown, Riyadh', 24.7136, 46.6753, 'Riyadh', 'Riyadh Province', 'Saudi Arabia', NOW(), NOW()),
(2, 'Al Tahlia Street, Shopping District, Riyadh', 24.7220, 46.6858, 'Riyadh', 'Riyadh Province', 'Saudi Arabia', NOW(), NOW()),
(3, 'Northern Ring Road, North Riyadh', 24.7500, 46.6900, 'Riyadh', 'Riyadh Province', 'Saudi Arabia', NOW(), NOW());

-- Insert Sample Menu Items
-- Breakfast Category
INSERT INTO items (name, description, price, calories, image_url, category_id, is_available, total_orders, is_best_seller, created_at, updated_at) VALUES
('Croissant with Butter', 'Freshly baked buttery croissant', 18.00, 280, 'https://images.unsplash.com/photo-1555507036-ab794f4aaef3?w=400', 1, true, 65, true, NOW(), NOW()),
('Pancakes with Syrup', 'Fluffy pancakes with maple syrup', 28.00, 450, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400', 1, true, 48, false, NOW(), NOW()),
('French Toast', 'Golden French toast with berries', 25.00, 380, 'https://images.unsplash.com/photo-1484723091739-30a097e8f929?w=400', 1, true, 52, true, NOW(), NOW()),
('Breakfast Sandwich', 'Egg, cheese, and bacon sandwich', 22.00, 420, 'https://images.unsplash.com/photo-1481070555726-e2fe8357725c?w=400', 1, true, 71, true, NOW(), NOW()),
('Avocado Toast', 'Smashed avocado on sourdough bread', 26.00, 320, 'https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=400', 1, true, 39, false, NOW(), NOW());

-- Drinks Category  
INSERT INTO items (name, description, price, calories, image_url, category_id, is_available, total_orders, is_best_seller, created_at, updated_at) VALUES
('Cappuccino', 'Rich espresso with steamed milk foam', 15.00, 120, 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?w=400', 2, true, 89, true, NOW(), NOW()),
('Fresh Orange Juice', 'Freshly squeezed orange juice', 18.00, 110, 'https://images.unsplash.com/photo-1613478223719-2ab802602423?w=400', 2, true, 67, true, NOW(), NOW()),
('Green Tea', 'Premium jasmine green tea', 12.00, 5, 'https://images.unsplash.com/photo-1627435601361-ec25f5b1d0e5?w=400', 2, true, 43, false, NOW(), NOW()),
('Iced Latte', 'Cold espresso with milk over ice', 16.00, 140, 'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=400', 2, true, 55, false, NOW(), NOW()),
('Smoothie Bowl', 'Mixed berry smoothie in a bowl', 24.00, 280, 'https://images.unsplash.com/photo-1511690743698-d9d85f2fbf38?w=400', 2, true, 38, false, NOW(), NOW());

-- Soups Category
INSERT INTO items (name, description, price, calories, image_url, category_id, is_available, total_orders, is_best_seller, created_at, updated_at) VALUES
('Chicken Noodle Soup', 'Traditional chicken soup with vegetables', 25.00, 320, 'https://images.unsplash.com/photo-1547592180-85f173990554?w=400', 3, true, 71, true, NOW(), NOW()),
('Lentil Soup', 'Hearty red lentil soup with Middle Eastern spices', 22.00, 280, 'https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400', 3, true, 58, true, NOW(), NOW()),
('Mushroom Cream Soup', 'Creamy mushroom soup with fresh herbs', 28.00, 350, 'https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?w=400', 3, true, 52, true, NOW(), NOW()),
('Tomato Basil Soup', 'Classic tomato soup with fresh basil', 20.00, 240, 'https://images.unsplash.com/photo-1547592180-85f173990554?w=400', 3, true, 31, false, NOW(), NOW()),
('Minestrone Soup', 'Italian vegetable soup with pasta', 23.00, 200, 'https://images.unsplash.com/photo-1547592180-85f173990554?w=400', 3, true, 29, false, NOW(), NOW());

-- Sushi Category
INSERT INTO items (name, description, price, calories, image_url, category_id, is_available, total_orders, is_best_seller, created_at, updated_at) VALUES
('Salmon Nigiri', 'Fresh salmon over seasoned rice (2 pieces)', 32.00, 140, 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400', 4, true, 89, true, NOW(), NOW()),
('California Roll', 'Crab, avocado, and cucumber roll (8 pieces)', 28.00, 255, 'https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=400', 4, true, 67, true, NOW(), NOW()),
('Tuna Sashimi', 'Fresh tuna slices (5 pieces)', 38.00, 180, 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400', 4, true, 43, false, NOW(), NOW()),
('Dragon Roll', 'Eel and cucumber with avocado on top', 45.00, 320, 'https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=400', 4, true, 55, false, NOW(), NOW()),
('Miso Soup', 'Traditional Japanese soybean soup', 15.00, 80, 'https://images.unsplash.com/photo-1547592180-85f173990554?w=400', 4, true, 38, false, NOW(), NOW());

-- Update best sellers based on total_orders (top 5)
UPDATE items SET is_best_seller = true WHERE id IN (
    SELECT id FROM (
        SELECT id FROM items ORDER BY total_orders DESC LIMIT 5
    ) AS top_items
);
