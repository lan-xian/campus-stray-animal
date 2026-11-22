// 公共工具：认证与请求封装
const API_BASE = 'http://localhost:8080';

/** 从 localStorage 取登录时保存的 token */
function getToken() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    return user && user.token ? user.token : null;
}

/** 带 JWT 的请求头 */
function authHeader() {
    return { 'Authorization': 'Bearer ' + getToken() };
}

/** 统一 fetch 封装：自动带 token，401 时跳转登录页 */
function apiFetch(url, options = {}) {
    options.headers = Object.assign({}, options.headers || {}, authHeader());
    return fetch(API_BASE + url, options)
        .then(res => res.json())
        .then(data => {
            if (data.code === 401) {
                localStorage.removeItem('user');
                window.location.href = redirectLogin();
                return null;
            }
            return data;
        });
}

/** 各页面回到对应的登录页路径 */
function redirectLogin() {
    const depth = window.location.pathname.includes('/admin/') || window.location.pathname.includes('/user/') ? '../login.html' : 'login.html';
    return depth;
}

/** 退出登录 */
function logout() {
    localStorage.clear();
    window.location.href = redirectLogin();
}
