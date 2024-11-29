## Notes

- Replace `{accountId}` with a valid account ID returned during registration.
- Base64 Basic Auth: Use `username:password` encoded as Base64 (e.g., `jankowalski:kowalski123` = `amFua293YWxza2k6a293YWxza2kxMjM=`).

----

## Endpoints

### 1. User Registration
Registers a new user account with an initial balance.

- **URL:** `POST /api/account/register`
- **Request Body:**
  ```json
  {
      "login": "jankowalski",
      "password": "kowalski123",
      "firstName": "Jan",
      "lastName": "Kowalski",
      "initialBalance": 1000.00
  }
  ```
- **Example cURL:**
  ```bash
  curl --location 'http://localhost:8080/api/account/register' \
  --header 'Content-Type: application/json' \
  --data '{
    "login": "jankowalski",
    "password": "kowalski123",
    "firstName": "Jan",
    "lastName": "Kowalski",
    "initialBalance": 1000.00
  }'
  ```

---

### 2. Fetch Account Details

- **URL:** `GET /api/account/{accountId}`
- **Authorization:** Basic Auth
- **Example cURL:**
  ```bash
  curl --location 'http://localhost:8080/api/account/{accountId}' \
  --header 'Authorization: Basic amFua293YWxza2k6a293YWxza2kxMjM='
  ```

---

### 3. Exchange Currency
Exchanges currency from the source currency to the target currency for a given account.

- **URL:** `POST /api/exchange`
- **Request Body:**
  ```json
  {
      "accountId": "{accountId}",
      "sourceCurrency": "PLN",
      "targetCurrency": "USD",
      "amount": 100.00
  }
  ```
- **Authorization:** Basic Auth
- **Example cURL:**
  ```bash
  curl --location 'http://localhost:8080/api/exchange' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic amFua293YWxza2k6a293YWxza2kxMjM=' \
  --data '{
      "accountId": "{accountId}",
      "sourceCurrency": "PLN",
      "targetCurrency": "USD",
      "amount": 100.00
  }'
  ```

---