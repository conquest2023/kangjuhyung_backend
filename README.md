# kangjuhyung_backend

###  환경 설정 (application.yml) 관련 안내
- 본 프로젝트의 `application.yml`은 보안상 `.gitignore`에 포함하는 것이 원칙이나, **과제 평가 시 별도의 설정 없이 즉시 실행 및 테스트 환경을 재현**할 수 있도록 의도적으로 포함하였습니다.
- 실제 운영 환경에서는 민감 정보를 분리하고 환경 변수(Environment Variables)를 통해 관리하는 설정을 지향합니다.

### Docker Compose를 사용한 실행 방법

프로젝트 루트 디렉토리에서 아래 명령어를 순서대로 실행하세요.

**1. 프로젝트 빌드 (JAR 생성)**
```bash
  ./gradlew clean build -x test
```
**2. Docker Compose 실행**

Bash
```bash
  docker-compose up --build -d
```

애플리케이션은 http://localhost:8080에서 실행됩니다.

H2 Console: http://localhost:81

JDBC URL: jdbc:h2:tcp://localhost:1521/opt/h2-data/test


## API 명세서



## Base URL



---

## 공통 사항

| Key | Value | Description |
|---|---|---|
| Base URL	| /api |	모든 요청의 기본 경로
| Content-Type | application/json | JSON 요청 |
| Authorization (선택) | Bearer {token} | 인증 확장 대비 (현재 미사용) |

---

## 계좌(Account) API

---

### 1. 계좌 등록

**[POST] /accounts**

새로운 계좌를 생성합니다.

**Request Body**
```json
{}
```
Response (201 Created)
```json
{
  "accountNumber": "1234-0333-1783-5334",
  "accountType": "CHECKING",
  "balance": 0,
  "status": "ACTIVE",
  "createdAt": "2026-01-02T18:29:02.927582"
}
```
### 계좌 상세 조회
**[GET] /accounts/{accountNumber}**

Response (200 OK)
```json
{
  "accountNumber": "110-1234-567890",
  "balance": 150000,
  "status": "ACTIVE",
  "lastTransactionAt": "2026-01-01T13:22:10"
}
```
### 계좌 삭제 (해지)
**[DELETE] /accounts/{accountNumber}**

Response (200 OK): "계좌가 삭제되었습니다"
## 2. 거래(Transaction) API
### 입금
**[POST] /accounts/deposits**

Request Body
```json
{
  "accountNumber": "110-1234-567890",
  "money": 50000
}
```
Response (200 OK)
```json
{
  "depositedAmount": 50000,
  "balanceBefore": 100000,
  "balanceAfter": 150000,
  "transactionAt": "2026-01-01T14:10:00"
}
```
### 출금
**[POST] /accounts/withdrawals**

Description: 일 한도 1,000,000원 제한

Request Body
```json
{
  "accountNumber": "110-1234-567890",
  "money": 30000
}
```
Response (200 OK)

```json
{
  "withdrawnAmount": 30000,
  "balanceBefore": 150000,
  "balanceAfter": 120000,
  "transactionAt": "2026-01-01T15:00:00",
  "todayWithdrawTotal": 30000,
  "dailyWithdrawLimit": 1000000,
  "todayWithdrawRemaining": 970000
}
```
### 이체
**[POST] /transfers**

Description: 수수료 1%, 일 한도 3,000,000원 제한

Request Body
```json
{
  "fromAccountNumber": "110-1234-567890",
  "toAccountNumber": "110-9999-888888",
  "amount": 50000
}
```
Response (200 OK)
```json
{
  "fromAccountNumber": "110-1234-567890",
  "toAccountNumber": "110-9999-888888",
  "transferAmount": 50000,
  "balanceAfterTransfer": 69500,
  "transactionAt": "2026-01-01T16:30:00",
  "status": "POSTED"
}
```
## 3. 거래내역 조회 API

### 거래내역 조회

**[GET] /{accountNumber}/histories**

특정 계좌의 거래내역을 최신순으로 조회합니다.

**Query Parameters**

| name | type | description |
|---|---|---|
| page | int | 페이지 번호 (0부터 시작) |
| size | int | 페이지 크기 |
| type | string | 입금 / 출금 / 송금 / 수취 (필수) |

**Response (200 OK)**
```json
{
  "content": [
      {
            "txId": 472,
            "accountId": 190,
            "counterpartyAccountId": 191,
            "type": "TRANSFER_OUT",
            "status": "POSTED",
            "direction": "OUT",
            "amount": "10000.0000",
            "balanceBefore": "100000.0000",
            "balanceAfter": "89900.0000",
            "createdAt": "2026-01-02T18:01:17.780589",
            "postedAt": "2026-01-02T18:01:17.779116"
        }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 3,
  "totalPages": 1
}
```
### 거래내역 상세(시간 및 type) 조회

**[GET] /accounts/{accountNumber}/histories**

**Query Parameters**

| name | type | description |
|---|---|---|
| page | int | 페이지 번호 (0부터 시작) |
| size | int | 페이지 크기 |
| type | string | 입금 / 출금 / 송금 / 수취 (선택) |
| preset | TimePreset | TODAY / LAST_1_HOUR / LAST_6_HOURS / LAST_7_DAYS / LAST_1_MONTH / LAST_3_MONTHS (선택) |


Response (200 OK)

```json
{
  "content": [
      {
            "txId": 472,
            "accountId": 190,
            "counterpartyAccountId": 191,
            "type": "TRANSFER_OUT",
            "status": "POSTED",
            "direction": "OUT",
            "amount": "10000.0000",
            "balanceBefore": "100000.0000",
            "balanceAfter": "89900.0000",
            "createdAt": "2026-01-02T18:01:17.780589",
            "postedAt": "2026-01-02T18:01:17.779116"
        }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 3,
  "totalPages": 1
}
```

## 에러 코드 및 응답

### 주요 에러 코드

| Code | HTTP Status | Description |
| :--- | :--- | :--- |
| ACCOUNT_NOT_FOUND | 404 | 계좌 없음 |
| INVALID_AMOUNT | 400 | 금액 오류 |
| DAILY_LIMIT_EXCEEDED | 409 | 한도 초과 |
| SELF_TRANSFER_NOT_ALLOWED | 400 | 자기 자신에게 이체 |
| INSUFFICIENT_BALANCE | 400 | 잔액 부족 |
