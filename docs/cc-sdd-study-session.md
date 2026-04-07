# cc-sdd 勉強会資料
## Claude Code × Kiro によるスペック駆動開発（Spec-Driven Development）

> 対象: バックエンド開発チーム
> 題材: `sample-travel-site-be` — お気に入り登録・解除API の実装

---

## 目次

1. [cc-sdd とは何か](#1-cc-sdd-とは何か)
2. [ワークフロー概観](#2-ワークフロー概観)
3. [Phase 1: プロジェクトの読み込み `kiro:steering`](#3-phase-1-プロジェクトの読み込み)
4. [Phase 2: 新機能のワークスペースを作成 `spec-init`](#4-phase-2-新機能のワークスペースを作成)
5. [Phase 3: 要件定義 `spec-requirements`](#5-phase-3-要件定義)
6. [Phase 4: 技術設計 `spec-design`](#6-phase-4-技術設計)
7. [Phase 5: タスク生成 `spec-tasks`](#7-phase-5-タスク生成)
8. [Phase 6: TDD 実装 `spec-impl`](#8-phase-6-tdd-実装)
9. [Phase 7: 実装検証 `validate-impl`](#9-phase-7-実装検証)
10. [ハマりポイントと解決策](#10-ハマりポイントと解決策)
11. [まとめ](#11-まとめ)

---

## 1. cc-sdd とは何か

**cc-sdd**（Claude Code Spec-Driven Development）とは、Claude Code の **Kiro スキル群** を使って
「要件定義 → 設計 → タスク分解 → TDD実装 → 検証」のプロセスで進める仕様書駆動開発ツールです。

> ※ スキルとは：Claudeに対して特定のタスクを実行するための手順や知識を追加する仕組み<br/>
> ※ Kiroとは：仕様駆動開発（Spec-driven development）ができるAI IDE

### 仕様書開発とは

AI駆動で開発を進めたことがある方は、「思っていたものと違うものができてしまい手戻りした」なんて経験があると思います。
AIは指示文（プロンプト）に足りない情報があっても、行間を読んで成果物を出してしまうので、成果物が思っていたものと違ったなんてことは結構起こりやすいです。

仕様書駆動開発とは、開発より先にAIに仕様書を作成させることで、AIとの認識齟齬を最小限にできる開発手法です。

### 従来の AI ペアプログラミングとの違い

| 比較軸 | 従来の AI コーディング | cc-sdd                         |
|--------|----------------------|--------------------------------|
| 起点 | 「〇〇を実装して」 | 構造化された要件・設計ドキュメント              |
| 実装の根拠 | 口頭指示・コンテキスト | `requirements.md` / `design.md` |
| テスト | 後付けが多い | TDD（テスト先行）強制                   |
| 検証 | 人手確認 | `validate-impl` で自動トレース        |
| 再現性 | 低い（会話依存） | 高い（仕様書がmarkdownで残る）            |

### 仕様書の構成

```
.kiro/
  specs/
    {feature-name}/
      spec.json          ← 承認状態・メタデータ管理
      requirements.md    ← EARS 形式の要件定義
      design.md          ← アーキテクチャ・インターフェース設計
      tasks.md           ← 実装タスクリスト（チェックボックス形式）
  steering/
    product.md           ← プロダクト概要（永続的プロジェクト知識）
    structure.md         ← パッケージ構成・命名規則
    tech.md              ← 技術スタック・規約
```

> **ポイント**: `.kiro/` はシンボリックリンクで外部リポジトリを指すことも可能。<br/>
> プロジェクト固有のルール（`CLAUDE.md` 等）と組み合わせて使う。

---

## 2. ワークフロー概観

```
/kiro:steering
         ↓  プロジェクト知識を読み込み・更新
/kiro:spec-init {feature概要}
         ↓  新機能のワークスペースを作成
/kiro:spec-requirements {feature}
         ↓  要件をレビュー・承認
/kiro:spec-design {feature}
         ↓  設計をレビュー・承認
/kiro:spec-tasks {feature}
         ↓  タスクをレビュー（承認は spec.json を手動更新 or -y フラグ）
/kiro:spec-impl {feature} {task-number}
         ↓  タスクを1つずつ TDD で実装
/kiro:validate-impl
         ↓  要件・設計・テストを総合検証
```

各フェーズは **独立したコンテキスト** で実行することが推奨されています。
特に `spec-impl` は1タスクごとにコンテキストをクリアすると効果的です。

---

## 3. Phase 1: プロジェクトの読み込み

### スラッシュコマンド

```
/kiro:steering
```

### 出力: `.kiro/steering/` 配下のファイル

`/kiro:steering` はコードベースを解析し、Claude Code が以降のすべてのフェーズで参照する **永続的なプロジェクト知識** を `.kiro/steering/` に生成・更新します。

生成されるファイルは3つです。

| ファイル | 内容 |
|---|---|
| `product.md` | プロダクトの目的・ドメイン言語・現在の実装状態 |
| `structure.md` | パッケージ構成・レイヤードアーキテクチャ・命名規則・DB規約 |
| `tech.md` | 技術スタック・ビルドツール・フレームワーク固有の規約 |

### 各ファイルの役割

**`product.md`** — ドメイン知識の定義

```markdown
## Domain Language

- **コンテンツ (Contents)**: Accommodation listings — the primary entity
- **お気に入り (Favorites)**: User-to-content bookmarks (unique per pair)
- **ログインユーザー**: `user_id=1` is treated as the logged-in user in seed data
```

要件定義フェーズで「お気に入り」「コンテンツ」の意味が正確に解釈されるよう、用語を統一します。

**`structure.md`** — アーキテクチャの制約

```markdown
## Layered Architecture

presentation → domain/service → domain/repository (interface) ← infrastructure

- dto/: OpenAPI Generator 自動生成（手書き不可）
- entity/: MyBatis マッピング用（Lombok @Data class）
- model/: ドメインモデル（Java record）
```

設計フェーズでクラス配置が規約に沿ったものになるよう制約を与えます。

**`tech.md`** — 技術選択の根拠

```markdown
## Key Decisions

- MyBatis over JPA: SQL を明示的に記述する
- H2 in-memory DB: schema.sql / data.sql で起動時に初期化
- Spock 2.4 (Groovy 4): テストは src/test/groovy/ 配下に記述
- OpenAPI スキーマ駆動開発: openapi.yaml → コード自動生成
```

実装フェーズで JPA を使ったコードを提案されたり、テストを Java で書かれたりしないよう、技術選択を固定します。

### なぜ steering が重要か

`steering/` がなければ、Claude Code はコードベースを毎回ゼロから解釈します。
`steering/` があれば、**プロジェクト固有の制約が最初から与えられた状態** で各フェーズが実行されます。

> **運用のポイント**: `steering/` はプロジェクトの成長に合わせて育てる。
> 新しい規約が決まったら `steering/tech.md` を更新し、次回から全フェーズに反映させる。

---

## 4. Phase 2: 新機能のワークスペースを作成

### スラッシュコマンド

```
/kiro:spec-init "お気に入り登録・解除APIの実装(feature-id: post-like)"
```

### 何をするのか

`/kiro:spec-init` は機能の概要テキストを受け取り、`.kiro/specs/{feature-id}/` 配下にスペックファイル群の雛形を作成します。

```
.kiro/specs/post-like/
  spec.json        ← 承認状態を管理するメタデータ（全フェーズ approved: false で初期化）
  requirements.md  ← 概要のみ記載（次フェーズで生成）
  design.md        ← 存在しない（次フェーズで生成）
  tasks.md         ← 存在しない（次フェーズで生成）
```

### なぜ先にワークスペースを作るのか

`spec-init` で **機能IDを確定** させることで、以降のすべてのコマンドが同じディレクトリを参照します。
機能の概要テキストは `spec.json` に記録され、`spec-requirements` が要件を生成する際のインプットになります。

```json
{
  "feature": "post-like",
  "description": "お気に入り登録・解除APIの実装",
  "approvals": {
    "requirements": { "generated": false, "approved": false },
    "design":       { "generated": false, "approved": false },
    "tasks":        { "generated": false, "approved": false }
  }
}
```

> **ポイント**: 機能IDはディレクトリ名になるため、小文字・ハイフン区切りで短く命名する。
> 以降のコマンドはすべて `post-like` という同じIDで実行する。

---

## 5. Phase 3: 要件定義

### スラッシュコマンド

```
/kiro:spec-requirements post-like
```

### 出力: `requirements.md`（抜粋）

EARS（Easy Approach to Requirements Syntax）形式で記述されます。

```markdown
**1.1** WHEN ユーザーが `POST /v1/contents/{contentId}/like` を呼び出したとき、
        システムは指定コンテンツを `t_favorites` テーブルに登録し、HTTP 201 を返す。

**1.2** WHEN ユーザーが既にお気に入り登録済みのコンテンツを再登録したとき、
        システムは HTTP 409 と `type: "FAVORITE_ALREADY_EXISTS"` を返す。
```

### EARS 形式の読み方

| キーワード | 意味 | 例 |
|-----------|------|----|
| `WHEN ... システムは` | 事象駆動 | 特定操作時の振る舞い |
| `IF ... システムは` | 条件駆動 | 特定状態での振る舞い |
| `The システムは` | ユビキタス | 常に成立する制約 |

### 要件番号の重要性

**全要件に数値 ID を付与すること** が必須です。
後続の `tasks.md` でトレーサビリティ（`_Requirements: 1.1, 1.3_`）を確保するために使われます。

---

## 6. Phase 4: 技術設計

### スラッシュコマンド

```
/kiro:spec-design post-like -y
```

### 出力: `design.md`（アーキテクチャ境界マップ抜粋）

```
[Client]
   ↓
[LikesController]  ← implements LikesApi (openapi.yaml から生成)
   ↓
[FavoritesService]  ← @Service
   ↓
[FavoritesRepository]  ← interface（domain 層、FW 依存なし）
   ↓
[FavoritesRepositoryImpl]  ← @Repository
   ├── [FavoritesMapper]  ← @Mapper (MyBatis)
   └── throws: ContentNotFoundException
               FavoriteNotFoundException
               FavoriteAlreadyExistsException

[GlobalExceptionHandler]
   ├── ContentNotFoundException      → 404 / CONTENT_NOT_FOUND
   ├── FavoriteNotFoundException     → 404 / FAVORITE_NOT_FOUND
   ├── FavoriteAlreadyExistsException → 409 / FAVORITE_ALREADY_EXISTS
   └── MethodArgumentTypeMismatchException → 400 / INVALID_PARAMETER
```

### Claudeとの対話で設計をブラッシュアップする

`spec-design` で生成した設計書は、そのままチャットでClaudeに修正を依頼できます。
仕様書は通常のMarkdownファイルなので、**Claudeと会話しながら内容を詰められる**のが cc-sdd の強みです。

**例: ErrorResponse に `type` フィールドを追加する**

404 エラーが `CONTENT_NOT_FOUND` と `FAVORITE_NOT_FOUND` の2種類ある場合、HTTPステータスコードだけではクライアントがエラー種別を区別できません。
そこで設計書の生成後に以下のようにClaudeへ追加指示を送ります。

> 「`ErrorResponse` に `type` フィールドを追加してください。
> HTTPステータスが同じ404でも `CONTENT_NOT_FOUND` と `FAVORITE_NOT_FOUND` のように
> エラー種別が異なるケースをクライアント側で識別できるようにしたいです。」

ClaudeはErrorResponseスキーマと各エラーハンドリングの設計を更新します。

```markdown
<!-- design.md 更新後の該当箇所 -->
### ErrorResponse スキーマ

| フィールド | 型 | 必須 | 説明 |
|---|---|---|---|
| type | string | ✅ | エラー種別の識別子（例: CONTENT_NOT_FOUND） |
| message | string | ✅ | エラーメッセージ |
```

> **ポイント**: 設計書への変更は `spec-requirements` の要件にも反映させる。
> 設計変更後は `spec-tasks` を再実行してタスクを更新するとトレーサビリティが保たれる。

### 設計書が解決すること

- **実装の「迷い」を排除**: クラス名・パッケージ・メソッドシグネチャが確定済み
- **境界の明確化**: どの層が何の責務を持つかが一目瞭然
- **影響範囲の可視化**: 新規追加8ファイル・既存修正2ファイルと事前に把握

---

## 7. Phase 5: タスク生成

### スラッシュコマンド

```
/kiro:spec-tasks post-like -y
```

### 出力: `tasks.md`（抜粋）

```markdown
- [ ] 1. (P) openapi.yaml を更新してスキーマとエンドポイントを定義する
- [ ] 1.1 ErrorResponse スキーマに type フィールドを必須追加する
  - _Requirements: 4.1, 4.3_
- [ ] 1.2 お気に入り登録エンドポイントを openapi.yaml に追加する
  - _Requirements: 1.1, 1.3, 1.4_

- [ ] 2. (P) ドメイン例外クラスを作成する  ← Task 1 と並列実行可能
- [ ] 3. Repository I/F と Service を実装する  ← Task 2 完了後
```

### タスク設計のルール

| ルール | 内容 |
|--------|------|
| 最大2階層 | メジャータスク + サブタスク（深いネストなし） |
| `(P)` マーカー | 並列実行可能なタスクに付与（番号の直後） |
| `_Requirements: X.X_` | サブタスクレベルで要件 ID を明示 |
| 実装順序の明示 | Task N 完了後に実行、と依存関係を記載 |

### 承認フロー

`spec.json` で各フェーズの承認状態を管理します。

```json
"approvals": {
  "requirements": { "generated": true, "approved": true },
  "design":       { "generated": true, "approved": true },
  "tasks":        { "generated": true, "approved": false }  ← 要承認
}
```

**`tasks.approved: false` のまま `spec-impl` を実行するとブロックされます。**
チームレビュー後に `true` に更新して次のフェーズへ進みます。

---

## 8. Phase 6: TDD 実装

### スラッシュコマンド（タスク番号を指定）

```bash
/kiro:spec-impl post-like 1   # Task 1 を実行
/kiro:spec-impl post-like 2   # Task 2 を実行
/kiro:spec-impl post-like 6,7 # Task 6 と 7 を並列実行
```

> **推奨**: コンテキスト肥大化を防ぐため、タスクごとにコンテキストをクリアする。

### TDD サイクル

各タスクは以下のサイクルで実行されます。

```
RED   → テストを先に書く（コードがないので当然 FAIL）
GREEN → テストを通す最小限のコードを書く
REFACTOR → コードを整理する
VERIFY → 全テストスイートを実行してリグレッションがないことを確認
MARK COMPLETE → tasks.md の [x] に更新
```

### 実装例: Task 2（ドメイン例外クラス）

**RED フェーズ — テスト先行**

```groovy
class DomainExceptionsSpec extends Specification {

    def "ContentNotFoundException — contentId がメッセージに含まれること"() {
        when:
        def ex = new ContentNotFoundException(99L)  // まだクラスが存在しない

        then:
        ex instanceof RuntimeException
        ex.message.contains("99")
    }
}
```

**GREEN フェーズ — 実装**

```java
public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(long contentId) {
        super("Content not found: " + contentId);
    }
}
```

### 実装例: Task 5（FavoritesRepositoryImpl）

複雑なロジック（コンテンツ存在確認 → 重複確認 → 挿入）を TDD で設計します。

```groovy
def "add — contentId が存在しないとき ContentNotFoundException がスローされること"() {
    given:
    favoritesMapper.countByContentId(99L) >> 0  // 存在しない

    when:
    repository.add(1L, 99L)

    then:
    thrown(ContentNotFoundException)
    0 * favoritesMapper.insert(*_)  // insert は呼ばれないこと
}
```

```java
@Override
public void add(long userId, long contentId) {
    if (favoritesMapper.countByContentId(contentId) == 0) {
        throw new ContentNotFoundException(contentId);
    }
    if (favoritesMapper.countByUserIdAndContentId(userId, contentId) > 0) {
        throw new FavoriteAlreadyExistsException(contentId);
    }
    favoritesMapper.insert(userId, contentId);
}
```

### テスト分類と使い分け

今回の実装で作成したテストの一覧です。

| テストクラス | 種別 | 手法 | テスト数 |
|---|---|---|---|
| `DomainExceptionsSpec` | 単体 | Pure Spock | 3 |
| `FavoritesServiceSpec` | 単体 | Mock (Spock) | 5 |
| `FavoritesMapperSpec` | Mapper | `@MybatisTest` + `@Sql` | 6 |
| `FavoritesRepositoryImplSpec` | 単体 | Mock (Spock) | 7 |
| `LikesControllerSpec` | Controller | `@WebMvcTest` + `@MockitoBean` | 6 |
| `LikesControllerIntegrationSpec` | 統合 | `@SpringBootTest` + `RestTemplate` | 5 |
| **合計（新規）** | | | **32** |

---

## 9. Phase 7: 実装検証

### スラッシュコマンド

```
/kiro:validate-impl
```

自動的に以下を検証します。

1. **タスク完了チェック** — `tasks.md` の全 `[x]` を確認
2. **テストカバレッジ** — テストスイートを実行して合否を確認
3. **要件トレーサビリティ** — 全12要件がコードに反映されているかを grep で確認
4. **設計整合チェック** — 設計書のファイル構成が実装に存在するか確認
5. **リグレッションチェック** — 既存テストが壊れていないか確認

### 今回の検証結果

```
✅ 21/21 サブタスク完了
✅ 64テスト / 0失敗（新規32 + 既存32）
✅ 12/12 要件カバレッジ 100%
✅ 設計ファイル全8件が実装済み
✅ リグレッションなし

判定: GO ✅
```

---

## 10. ハマりポイントと解決策

実際のセッションで遭遇した問題を共有します。

### ① `.kiro` ディレクトリがシンボリックリンク

**問題**: `spec-impl` がスペックファイルを見つけられなかった。

```
.kiro → /Users/.../common-task-document/kiro/sample-travel-site/sample-travel-site-be
```

**解決**: 実体パスを直接確認する。
`ls -la .kiro` でリンク先を確認してからファイル操作。

---

### ② `tasks.approved: false` によるブロック

**問題**: `spec-impl` を実行しても「Tasks Not Approved」でブロックされる。

**解決**: `spec-tasks` を再実行してタスクを再生成 → チームでレビュー後 `spec.json` を更新。

```json
"tasks": { "generated": true, "approved": true }
```

> **教訓**: タスク生成後に必ずチームレビューを挟む。`approved: false` はそのための意図的な設計。

---

### ③ Spock の多段 `then:` ブロックでスタブが無効化される

**問題**: 以下のテストで `ContentNotFoundException` が意図せずスローされた。

```groovy
// NG: given: のスタブが then: の interaction に上書きされる
given:
favoritesMapper.countByContentId(10L) >> 1  // ← 無視される

when:
repository.add(1L, 10L)

then:
1 * favoritesMapper.countByContentId(10L)  // 戻り値未指定 → デフォルト値 (0) を返す
```

**解決**: `then:` ブロック内でスタブと interaction を同時に定義する。

```groovy
// OK: >> でスタブと verification を同時指定
when:
repository.add(1L, 10L)

then:
1 * favoritesMapper.countByContentId(10L) >> 1  // 1を返しつつ1回呼ばれたことを検証

then:
1 * favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 0

then:
1 * favoritesMapper.insert(1L, 10L)
```

> **Spock の仕様**: `then:` ブロックに書いた interaction は `given:` のスタブより優先される。
> 多段 `then:` で実行順序を検証するときは、`then:` 内でスタブ値も指定する。

---

### ④ `groovy.json.JsonSlurper` が依存に含まれない

**問題**: 統合テストで JSON をパースしようとしたが、`groovy-json` モジュールが依存にない。

```groovy
// NG: コンパイルエラー
import groovy.json.JsonSlurper  // unable to resolve class
```

**原因**: `build.gradle` に `org.apache.groovy:groovy` のみで `groovy-json` が未追加。

**解決**: エラーをスローしない RestTemplate を使い、JSON パース自体を回避する。

```groovy
// OK: Map 型で直接レスポンスを受け取る
private static RestTemplate buildNoThrowRestTemplate() {
    def template = new RestTemplate()
    template.errorHandler = [
        hasError   : { response -> false },
        handleError: { response -> }
    ] as ResponseErrorHandler
    return template
}

// 使用時
def response = noThrowRestTemplate.postForEntity(url, null, Map)
assert response.statusCode.value() == 404
assert response.body.type == "CONTENT_NOT_FOUND"  // Map として直接アクセス
```

> **選択肢**: `groovy-json` を `testImplementation` に追加する方法もある。
> `org.apache.groovy:groovy-json:4.0.24`

---

### ⑤ Groovy での `ResponseErrorHandler` 無名クラス実装

**問題**: `@Override` アノテーションが「インターフェースのメソッドをオーバーライドしていない」エラーになる。

**解決**: Groovy の Map-to-interface コーション（`as ResponseErrorHandler`）を使う。

```groovy
// NG: Java スタイルの無名クラス
new ResponseErrorHandler() {
    @Override
    boolean hasError(ClientHttpResponse r) { false }
    @Override
    void handleError(ClientHttpResponse r) {}  // コンパイルエラー
}

// OK: Groovy の Map コーション
[
    hasError   : { response -> false },
    handleError: { response -> }
] as ResponseErrorHandler
```

---

## 11. まとめ

### cc-sdd がもたらす価値

```
ドキュメントが残る
  → 要件・設計・タスクが .kiro/specs/ に永続化される
  → 「なぜこう実装したか」が後から追跡できる

TDD が強制される
  → テスト先行で書くことで、インターフェースが自然と整理される
  → 64テスト / 0失敗の状態で機能完成

要件トレーサビリティが自動化される
  → validate-impl が全12要件を grep で確認
  → 「実装し忘れ」がゼロになる

実装粒度が適切に保たれる
  → タスクを1つずつ実行するルールがコンテキスト肥大化を防ぐ
```

### 今回の数字

| 指標 | 値 |
|---|---|
| 実装フェーズ数 | 8タスク（21サブタスク）|
| 新規実装ファイル数 | 8ファイル |
| 既存修正ファイル数 | 2ファイル |
| 新規テスト数 | 32テスト |
| 全テスト数（合計）| 64テスト |
| 要件カバレッジ | 12/12（100%）|
| リグレッション | 0件 |

### チームへの推奨プラクティス

1. **フェーズ間にチームレビューを挟む**
   `approved: false` の状態でレビューを行い、合意してから次フェーズへ。

2. **`spec-impl` は1タスクずつ、コンテキストをクリアして実行**
   長いコンテキストはモデルのパフォーマンスを下げる。

3. **`steering/` ファイルをプロジェクト固有の知識として育てる**
   `tech.md` / `structure.md` に実際の規約を反映しておくと生成精度が向上する。

4. **テスト失敗時は `spec-impl` を止めて原因を調査**
   「とりあえず動かす」ために `@Override` を外したり mock を多用するのは避ける。

5. **スペックファイルはコードと同じリポジトリで管理する**
   設計と実装が乖離しないよう、変更時はスペックも更新する。

---

## 参考: 今回の実装で作成したファイル一覧

### プロダクションコード

```
src/main/java/com/sample/travel/
  domain/
    exception/
      ContentNotFoundException.java        ← Task 2
      FavoriteNotFoundException.java       ← Task 2
      FavoriteAlreadyExistsException.java  ← Task 2
    repository/
      FavoritesRepository.java             ← Task 3
    service/
      FavoritesService.java                ← Task 3
  infrastructure/
    mapper/
      FavoritesMapper.java                 ← Task 4
    repository/
      FavoritesRepositoryImpl.java         ← Task 5
  presentation/
    controller/
      LikesController.java                 ← Task 6
    handler/
      GlobalExceptionHandler.java          ← Task 7（更新）
src/main/resources/
  openapi.yaml                             ← Task 1（更新）
```

### テストコード

```
src/test/groovy/com/sample/travel/
  domain/
    exception/
      DomainExceptionsSpec.groovy          ← Task 2
    service/
      FavoritesServiceSpec.groovy          ← Task 3
  infrastructure/
    mapper/
      FavoritesMapperSpec.groovy           ← Task 4
    repository/
      FavoritesRepositoryImplSpec.groovy   ← Task 5
  presentation/
    controller/
      LikesControllerSpec.groovy           ← Task 6
      LikesControllerIntegrationSpec.groovy ← Task 8
      ContentsControllerSpec.groovy        ← Task 7（更新）
src/test/resources/
  sql/mapper/
    FavoritesMapper.sql                    ← Task 4
```
