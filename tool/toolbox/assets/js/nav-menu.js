/**
 * 导航菜单交互功能
 */
document.addEventListener('DOMContentLoaded', function() {
    // 获取导航菜单元素
    const navMenu = document.querySelector('.nav-menu');
    const navMenuToggle = document.querySelector('.nav-menu-toggle');
    
    if (!navMenu || !navMenuToggle) return;
    
    // 获取当前工具ID
    function getCurrentToolId() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('tool');
    }
    
    // 切换导航菜单的显示/隐藏
    navMenuToggle.addEventListener('click', function(event) {
        navMenu.classList.toggle('active');
        
        // 当菜单打开时，检查并更新最常使用列表
        if (navMenu.classList.contains('active')) {
            const currentToolId = getCurrentToolId();
            if (currentToolId) {
                window.toolUsageManager.recordToolUsage(currentToolId);
            }
        }
        
        // 阻止事件冒泡，避免触发document的点击事件处理
        event.stopPropagation();
    });
    
    // 点击菜单外部时关闭菜单
    document.addEventListener('click', function(event) {
        const isClickInside = navMenu.contains(event.target) || navMenuToggle.contains(event.target);
        if (!isClickInside && navMenu.classList.contains('active')) {
            navMenu.classList.remove('active');
        }
    });

    // 更新最近使用工具菜单
    function updateRecentToolsMenu() {
        const menuContainer = document.getElementById('most-used-menu');
        if (!menuContainer) return;
        
        menuContainer.innerHTML = '';
        
        const recentTools = window.toolUsageManager.getMostUsedTools();
        
        if (recentTools.length === 0) {
            const li = document.createElement('li');
            li.className = 'nav-menu-item';
            li.innerHTML = '<span class="nav-menu-link">暂无使用记录</span>';
            menuContainer.appendChild(li);
            return;
        }
        
        recentTools.forEach(toolId => {
            // 查找原始工具链接以获取其信息
            const originalLink = document.querySelector(`.nav-menu-link[data-tool="${toolId}"]`);
            if (!originalLink) return;

            const li = document.createElement('li');
            li.className = 'nav-menu-item';
            li.innerHTML = `
                <a href="#" class="nav-menu-link" data-tool="${toolId}" data-src="${originalLink.dataset.src}">
                    ${originalLink.innerHTML}
                </a>
            `;
            menuContainer.appendChild(li);
        });

        // 重新绑定事件
        menuContainer.querySelectorAll('.nav-menu-link[data-tool]').forEach(link => {
            link.addEventListener('click', handleToolClick);
        });
    }

    // 处理工具点击事件
    function handleToolClick(e) {
        e.preventDefault();
        const toolId = this.dataset.tool;
        
        // 记录工具使用
        window.toolUsageManager.recordToolUsage(toolId);
        
        // 加载工具
        const contentFrame = document.getElementById('content-frame');
        if (contentFrame) {
            contentFrame.src = this.dataset.src;
        }
        
        // 更新活动状态
        document.querySelectorAll('.nav-menu-link').forEach(l => l.classList.remove('active'));
        this.classList.add('active');
        
        // 关闭菜单
        navMenu.classList.remove('active');
    }

    // 监听工具点击
    document.querySelectorAll('.nav-menu-link[data-tool]').forEach(link => {
        link.addEventListener('click', handleToolClick);
    });

    // 监听工具使用记录更新事件
    window.addEventListener('toolUsageUpdated', updateRecentToolsMenu);

    // 监听storage事件，当其他页面更新了localStorage时更新导航菜单
    window.addEventListener('storage', function(e) {
        if (e.key === window.toolUsageManager.STORAGE_KEY) {
            updateRecentToolsMenu();
        }
    });

    // 初始化最近使用工具菜单
    updateRecentToolsMenu();

    // 初始化时记录当前工具
    const currentToolId = getCurrentToolId();
    if (currentToolId) {
        window.toolUsageManager.recordToolUsage(currentToolId);
    }
}); 