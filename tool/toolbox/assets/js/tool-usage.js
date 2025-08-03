/**
 * 工具使用记录管理器
 */
const toolUsageManager = {
    STORAGE_KEY: 'tool_usage_records',
    MAX_RECENT_TOOLS: 3,

    // 获取工具使用记录
    getToolUsage() {
        const usage = localStorage.getItem(this.STORAGE_KEY);
        return usage ? JSON.parse(usage) : {};
    },

    // 记录工具使用
    recordToolUsage(toolId) {
        if (!toolId) return;
        
        const usage = this.getToolUsage();
        usage[toolId] = Date.now();
        localStorage.setItem(this.STORAGE_KEY, JSON.stringify(usage));
        
        // 触发自定义事件通知其他页面更新
        window.dispatchEvent(new CustomEvent('toolUsageUpdated'));

        // 触发一个storage事件来通知当前页面
        window.dispatchEvent(new StorageEvent('storage', {
            key: this.STORAGE_KEY,
            newValue: JSON.stringify(usage),
            oldValue: null,
            storageArea: localStorage
        }));
    },

    // 获取最常用工具列表
    getMostUsedTools() {
        const usage = this.getToolUsage();
        return Object.entries(usage)
            .sort(([, a], [, b]) => b - a) // 按时间戳倒序排序
            .slice(0, this.MAX_RECENT_TOOLS)
            .map(([toolId]) => toolId);
    },

    // 格式化日期
    formatDate(date) {
        const now = new Date();
        const diff = now - date;
        
        // 1分钟内
        if (diff < 60000) {
            return '刚刚';
        }
        // 1小时内
        if (diff < 3600000) {
            return Math.floor(diff / 60000) + '分钟前';
        }
        // 24小时内
        if (diff < 86400000) {
            return Math.floor(diff / 3600000) + '小时前';
        }
        // 超过24小时
        return new Date(date).toLocaleDateString();
    },

    // 获取工具的最后使用时间
    getLastUsedTime(toolId) {
        const usage = this.getToolUsage();
        return usage[toolId] ? this.formatDate(new Date(usage[toolId])) : null;
    }
};

// 导出工具使用记录管理器
window.toolUsageManager = toolUsageManager; 