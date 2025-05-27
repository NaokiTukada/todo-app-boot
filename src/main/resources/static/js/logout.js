function logout() {
    localStorage.removeItem("jwtToken"); // JWTを削除
    window.location.href = "/login";   // ログイン画面に戻す
}