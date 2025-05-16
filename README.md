# 概要
- アプリ名は習慣ToDoリスト
- 日々習慣付けをしたいタスクを管理するためのWebアプリ

# ドキュメントの場所
https://drive.google.com/drive/folders/1QjzJ9VN7L_G_fvTh885Sfn-unPxVzsaf

# 事前にインストールが必要なソフト
- Java 24
- MySQL 8.0以上
- VSCode拡張機能[Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack)拡張機能をインストールする

# ローカルでの環境構築手順
1. MySQLに新しくDBを作成する
    - `mysql -uroot -p`
    - `create database <DB名>;`
1. リポジトリのルートディレクトリにある.env.exampleファイルを複製して.envファイルを作る
    - ファイルの中身の各行の右辺は各自のローカル環境に合わせて変更する

# ローカルでの実行手順
1. MySQLを起動する
2. VSCodeの左のタブから「Spring Boot」タブを開く
3. 「todoapp」の行の三角ボタンを押す
    - ターミナルが開く
4. ターミナルに「Started TodoappApplication」と表示されるまで待つ
    - このとき、エンティティの定義に沿って接続先のDB内に自動でテーブルが作成される
5. ブラウザで`http://localhost:8080`にアクセスする
    - ログイン画面が表示される