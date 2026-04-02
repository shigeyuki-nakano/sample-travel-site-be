# Project Rules

## Tech Stack

- **Java 17** / **Spring Boot 4.0.3** (`spring-boot-starter-webmvc`)
- **MyBatis** (`mybatis-spring-boot-starter:4.0.1`) — JPA/Hibernate は使わない
- **H2** in-memory DB (`jdbc:h2:mem:traveldb`) — 開発・テスト共通
- **Lombok** — `@Data`, `@RequiredArgsConstructor`, `@Builder` 等でボイラープレート削減
- **Spock 2.4 (Groovy 4)** — テストフレームワーク（`src/test/groovy/` 配下）
- **OpenAPI Generator** (v7.6.0) — `openapi.yaml` からDTO・APIインターフェースを自動生成

## その他

- 設定ファイルは `application.yaml`（`.properties` は使わない）
- `spring.jackson.default-property-inclusion: non_null` — null フィールドはJSONレスポンスに含めない
- 認証未実装のため `user_id=1` をログインユーザーとして固定で扱う
