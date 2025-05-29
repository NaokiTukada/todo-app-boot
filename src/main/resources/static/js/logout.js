function logout() {
  fetch("/api/auth/logout", {
        method: "POST",
    }).then(() => {
        location.href = "/login";
    });
}