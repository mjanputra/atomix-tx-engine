-- KEYS[1] = balance key (e.g., "balance:u1")
-- ARGV[1] = amountCents to debit

local currentBalance = redis.call("GET", KEYS[1])
if not currentBalance then 
    bal = "0"
end

local balance = tonumber(currentBalance)
local amount = tonumber(ARGV[1])

if not amount then
    return -2 -- Invalid amount
end

-- If there is insufficient balance, return an error
if balance < amount then 
    return -1
end

-- If sufficient, debit the amount
local newBalance = balance - amount
redis.call("SET", KEYS[1], tostring(newBalance))
return newBalance