# WOVN.io Java ライブラリ

WOVN.io Java ライブラリは Java アプリケーションで WOVN.io ライブラリ方式の翻訳を実現するライブラリです。WOVN.io Java ライブラリは Servlet Filter として実装されています。

本ドキュメントは WOVN.io Java ライブラリのインストール手順と、設定パラメータについて説明します。

## 1. インストール手順

### 1.1. WOVN.io のアカウント作成

WOVN.io Java ライブラリを使用するためには、WOVN.io のアカウントが必要です。 アカウントをお持ちでない場合は、まず [WOVN.io](https://wovn.io) にてサインアップをしてください。

### 1.2. 翻訳ページの追加

[WOVN.io](https://wovn.io) にサインインをして、翻訳したいページを追加してください

### 1.3. Java アプリケーションの設定

#### 1.3.1. Maven の場合

※ Maven 以外をお使いの場合は、こちらの設定方法をご覧ください。(https://jitpack.io/#wovnio/wovnjava)

##### 1.3.1.1. 本ライブラリを組み込むアプリケーションの pom.xml に、JitPack のリポジトリを追加してください。

```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
  
  <!-- SNAPSHOT バージョンを使用しない場合は、以下の行は必要ありません。 -->
  <snapshots>
    <enabled>true</enabled>
    <updatePolicy>always</updatePolicy>
  </snapshots>
  <!-- end -->
  
</repositories>
```

##### 1.3.1.2. アプリケーションの pom.xml の依存関係に、本ライブラリを追加してください。

```XML
<dependency>
  <groupId>com.github.wovnio</groupId>
  <artifactId>wovnjava</artifactId>
  <version>-SNAPSHOT</version><!-- 使用するライブラリのバージョンを指定してください。 -->
</dependency>
```

使用可能なライブラリのバージョンはこちらのページで確認できます。(https://jitpack.io/#wovnio/wovnjava)

##### 1.3.1.3. ライブラリの設定を Servlet の web.xml に記述してください。

```XML
<filter>
  <filter-name>wovn</filter-name>
  <filter-class>com.github.wovnio.wovnjava.WovnServletFilter</filter-class>
  <init-param>
    <param-name>userToken</param-name>
    <param-value>2Wle3</param-value><!-- ユーザートークンを設定してください。 -->
  </init-param>
  <init-param>
    <param-name>secretKey</param-name>
    <param-value>secret</param-value><!-- シークレットキーとして何か適当な値を指定してください。 -->
  </init-param>
</filter>

<filter-mapping>
  <filter-name>wovn</filter-name>
  <url-pattern>/*</url-pattern><!-- ライブラリ (Servlet Filter) を適用する URL パターンを設定してください。 -->
</filter-mapping>
```

## 2. 設定パラメータ

WOVN.io Java ライブラリに設定可能なパラメータは以下の通りです。

パラメータ名 | 必須かどうか | 初期値
------------ | ------------ | ------------
userToken    | yes          | ''
secretKey    | yes          | ''
urlPattern   | yes          | 'path'
query        |              | ''
defaultLang  | yes          | 'en'

※ 初期値が設定されている必須パラメータは、web.xml で設定しなくても大丈夫です。

### 2.1. userToken

あなたの WOVN.io アカウントのユーザートークンを設定してください。このパラメータは必須です。

### 2.2. secretKey

このパラメータは開発中で、現在は未使用です。必須パラメータですので「secret」など、何かしらの文字を設定してください。

### 2.3. urlPattern

ライブラリは Java アプリケーションに対し、翻訳ページ用の新しい URL を追加します。urlPattern パラメータでは、この URL のタイプを設定できます。URL のタイプには下記の3種類があります。

パラメータ  | 翻訳ページの URL                | 備考
----------- | ------------------------------- | ------
'path'      | https://wovn.io/ja/contact      | 初期値。設定しない場合はこれになります。
'subdomain' | https://ja.wovn.io/contact      | DNS の設定が必要です。
'query'     | https://wovn.io/contact?wovn=ja | アプリケーションの変更が一番少なく済みます。

※ 上記翻訳ページの URL は、下記のページをライブラリで翻訳した場合の例です。

    https://wovn.io/contact

### 2.4. query

query パラメータでは、WOVN.io で翻訳するページの URL について、無視したいクエリパラメータを設定することができます。初期値は何も設定されておらず、この場合は全てのクエリパラメータを翻訳するページの URL に含めます。

    https://wovn.io/ja/contact?os=mac&keyboard=us

defualt_lang が 'en' で、query が '' （未設定）の場合、上記の URL は下記のように変換されて、翻訳ページを探します。

    https://wovn.io/contact?os=mac&keyboard=us

default_lang が 'en' で、query が 'mac' の場合、上記の URL は下記のように変換されて、翻訳ページを探します。

    https://wovn.io/contact?os=mac

query パラメータを複数設定したい場合は、カンマ区切りで指定してください。

### 2.5. defaultLang

Java アプリケーションの言語を設定してください。初期値は英語 ('en') です。

デフォルト言語のページへパラメータ付きでアクセスがあった場合、ライブラリは翻訳前の URL にリダイレクトします。defaultLang はこの処理に使用されます。

defaultLang が 'en' で下記 URL にリクエストがあった場合、

    https://wovn.io/en/contact

ライブラリは、下記 URL にリダイレクトします。

    https://wovn.io/contact