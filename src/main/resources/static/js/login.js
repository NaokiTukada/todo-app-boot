// ログイン画面のJavaScript

// 各HTML要素を取得
const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const errorMessageElement = document.getElementById('errorMessage');
const registerLink = document.querySelector('.register-link'); // 新規登録リンク

// エラーメッセージを表示する関数
function showErrorMessage(message) {
    errorMessageElement.textContent = message;
    errorMessageElement.style.display = 'block';
}

// エラーメッセージを非表示にする関数
function hideErrorMessage() {
    errorMessageElement.textContent = '';
    errorMessageElement.style.display = 'none';
}

// 「ログイン」ボタンがクリックされた（フォームが送信された）時の処理
loginForm.addEventListener('submit', async function(event) {
    // フォームのデフォルト送信動作を停止
    event.preventDefault();

    hideErrorMessage(); // まずエラーメッセージを非表示にする

    const email = emailInput.value;
    const password = passwordInput.value;

    // メールアドレス、パスワードがともに記載されているかチェック
    // 記載されていない場合は、認証処理には進まず、バックエンドからのエラーを待つ

    if (!email || !password) {
        // 認証処理へ進まないが、エラーメッセージはバックエンドからの応答に依存
        // ここで何も表示しないことで、仕様書に準拠
        // 例えば、バックエンドが空欄で401を返すなら、そのメッセージが表示される
    }

    // バックエンドへ送信するデータ
    const loginData = {
        email: email,
        password: password
    };

    try {
        // バックエンドの/api/auth/loginエンドポイントへPOSTリクエストを送信
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        // レスポンスのHTTPステータスを確認
        if (response.ok) {
            // ログイン成功後、目標一覧表画面へリダイレクト
            window.location.href = '/tasks'; 
        } else {
            // エラーレスポンスの場合
            const errorText = await response.text(); // エラーメッセージをテキストとして取得
            let displayMessage = 'ログイン処理中にエラーが発生しました'; // 予期せぬエラーのデフォルトメッセージ

            try {
                // もしレスポンスがJSON形式のエラーメッセージならパースを試みる
                const errorJson = JSON.parse(errorText);
                // バックエンドからの具体的なエラーメッセージがあればそれを使用
                if (errorJson.message) {
                    displayMessage = errorJson.message;
                } else if (typeof errorJson === 'string') {
                    // シンプルな文字列がJSONとして返された場合
                    displayMessage = errorJson;
                }
            } catch (e) {
                // JSONではない場合は、生のテキストがエラーメッセージとして使える場合がある
                if (errorText) {
                    displayMessage = errorText;
                }
            }

            // エラーメッセージを優先
            if (response.status === 401) { // Unauthorized (認証失敗)
                showErrorMessage('メールアドレス、パスワードが誤っている可能性があります');
            } else {
                showErrorMessage(displayMessage);
            }
        }
    } catch (error) {
        // ネットワークエラーなど、リクエスト自体が失敗した場合
        console.error('ログイン中にネットワークエラーが発生しました:', error);
        showErrorMessage('ログイン処理中にエラーが発生しました'); // ネットワークエラーメッセージ
    }
});