// 新規登録画面のJavaScript

// 各HTML要素を取得
const registrationForm = document.getElementById('registrationForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const confirmInput = document.getElementById('confirm');
const errorMessageElement = document.getElementById('errorMessage');
const backButton = document.getElementById('backButton');
const completeButton = document.getElementById('completeButton');

// エラーメッセージを表示する関数 (複数のメッセージに対応)
function showErrorMessages(messages) {
    errorMessageElement.innerHTML = '';
    messages.forEach(message => {
        const line = document.createElement('div');
        line.className = 'error-line';
        line.textContent = message;
        errorMessageElement.appendChild(line);
    });
    errorMessageElement.style.display = 'block';
}

// エラーメッセージを非表示にする関数
function hideErrorMessage() {
    errorMessageElement.textContent = '';
    errorMessageElement.style.display = 'none';
}

// 「完了」ボタンがクリックされた時の処理
registrationForm.addEventListener('submit', async function(event) {
    
    event.preventDefault();

    hideErrorMessage(); // まずエラーメッセージを非表示にする

    const email = emailInput.value;
    const password = passwordInput.value;
    const confirmPassword = confirmInput.value;

    const errors = []; 


    // 1. パスワードの文字数チェック (8文字以上、256文字以内)
    if (password.length < 8 || password.length > 256) {
        errors.push('パスワードは８文字以上で設定してください');
    }

    // 2. メールアドレスの形式チェック (XXXX@XXX.XXXの形式)
    // 入力欄は256文字以内はHTMLのmaxlengthで対応
    if (!/\S+@\S+\.\S+/.test(email)) {
        errors.push('XXXX@XXX.XXXの形式で入力してください');
    }

    // 3. パスワードと確認用の一致チェック
    if (password !== confirmPassword) {
        errors.push('パスワードと確認用が異なってる可能性があります');
    }

    // もしエラーが1つでもあれば、ここで表示して処理を中断
    if (errors.length > 0) {
        showErrorMessages(errors);
        return;
    }

    // クライアントサイドのバリデーション（検証）を通過したら、バックエンドへ送信
    const registrationData = {
        email: email,
        password: password,
        confirmPassword: confirmPassword
    };

    try {
        // バックエンドの/api/auth/registerエンドポイントへPOSTリクエストを送信
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registrationData)
        });

        // レスポンスのHTTPステータスを確認
        if (response.ok) { // HTTPステータスが2xxの場合 
            // 登録成功後、ログインページへリダイレクト
            window.location.href = '/login'; // 実際のログインページのパスに修正してください
        } else {
            // エラーレスポンスの場合
            const errorText = await response.text(); // エラーメッセージをテキストとして取得
            let displayMessage = '新規登録処理中にエラーが発生しました'; // 予期せぬエラーのデフォルトメッセージ

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
            showErrorMessages([displayMessage]); // バックエンドからのエラーは1つずつ表示
        }
    } catch (error) {
        // ネットワークエラーなど、リクエスト自体が失敗した場合
        console.error('登録中にネットワークエラーが発生しました:', error);
        showErrorMessages(['新規登録処理中にエラーが発生しました']); // ネットワークエラーメッセージ
    }
});

// 「戻る」ボタンがクリックされた時の処理
backButton.addEventListener('click', function() {
    // ログイン画面へ遷移
    window.location.href = '/login';
});