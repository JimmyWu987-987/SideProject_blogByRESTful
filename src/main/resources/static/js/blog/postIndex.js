// 1. 定義後端 API 網址 (本地端)
const API_URL = 'http://localhost:8080/api/v1/posts';

// 2. 選取 HTML 容器
const postsContainer = document.getElementById('posts-container');

// 3. 執行 Fetch 請求
function fetchPosts() {
    fetch(API_URL) // 發送 GET 請求
        .then(response => {
            // 檢查回應是否成功 (Status 200 OK)
            if (!response.ok) {
                throw new Error('網路回應失敗 (Network response was not ok)');
            }
            return response.json(); // 將回應轉為 JSON 物件
        })
        .then(data => {
            // Spring Boot 的 Page 物件，文章列表是在 "content" 屬性裡面
            const posts = data.content; 
            
            renderPosts(posts); // 呼叫渲染函式
        })
        .catch(error => {
            console.error('Fetch error:', error);
            postsContainer.innerHTML = '<p style="color:red">無法載入文章，請確認後端是否已啟動。</p>';
        });
}

// 4. 渲染畫面函式
function renderPosts(posts) {
    // 如果沒有文章
    if (posts.length === 0) {
        postsContainer.innerHTML = '<p>目前沒有任何文章。</p>';
        return;
    }

    // 清空容器
    postsContainer.innerHTML = '';

    // 跑迴圈產生 HTML
    // 依據 PostVO 的欄位: title, content, createTime, userId
    posts.forEach(post => {
        const postElement = document.createElement('div');
        postElement.classList.add('post-card');

        // 使用 Template String (反引號) 組合 HTML
        // 注意：這裡對應你的 PostVO 欄位名稱
        postElement.innerHTML = `
            <h2 class="post-title">${post.title}</h2>
            <div class="post-meta">
                作者 ID: ${post.userId} | 
                發布時間: ${new Date(post.createTime).toLocaleString()}
            </div>
            <div class="post-content">
                ${post.content}
            </div>
            <hr>
            <button onclick="alert('文章ID: ${post.postId}')">查看詳情</button>
        `;

        // 將做好的卡片加入容器
        postsContainer.appendChild(postElement);
    });
}

// 頁面載入後立即執行
fetchPosts();