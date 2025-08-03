// 初始化 diff_match_patch 实例
const dmp = new diff_match_patch();

// DOM 元素
let elements = null;

// 状态管理
const state = {
    isJsonMode: false,
    lastLeftText: '',
    lastRightText: '',
    updateTimeout: null,
    notifications: []
};

// 工具函数
const utils = {
    // 防抖函数
    debounce(func, wait) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    },
    
    // 检查是否为有效的 JSON
    isValidJSON(str) {
        if (!str || typeof str !== 'string' || !str.trim()) return false;
        try {
            JSON.parse(str);
            return true;
        } catch (e) {
            return false;
        }
    },
    
    // 格式化 JSON
    formatJSON(str) {
        try {
            return JSON.stringify(JSON.parse(str), null, 2);
        } catch (e) {
            return str;
        }
    },
    
    // HTML 转义
    escapeHTML(text) {
        return text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    },
    
    // 创建通知元素
    createNotification(message, isError = false) {
        const notification = document.createElement('div');
        notification.className = `notification${isError ? ' error' : ''}`;
        notification.textContent = message;
        return notification;
    }
};

// 通知管理
const notifications = {
    show(message, isError = false, duration = 3000) {
        const notification = utils.createNotification(message, isError);
        document.body.appendChild(notification);
        
        // 触发重排以启动动画
        notification.offsetHeight;
        notification.classList.add('show');
        
        // 添加到通知列表
        const notificationInfo = {
            element: notification,
            timeout: setTimeout(() => {
                this.hide(notification);
            }, duration)
        };
        
        state.notifications.push(notificationInfo);
        
        // 限制最大通知数量
        if (state.notifications.length > 3) {
            const oldestNotification = state.notifications.shift();
            this.hide(oldestNotification.element);
            clearTimeout(oldestNotification.timeout);
        }
    },
    
    hide(notification) {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
            state.notifications = state.notifications.filter(n => n.element !== notification);
        }, 300);
    }
};

// 编辑器管理
const editor = {
    // 更新行号
    updateLineNumbers(text, lineNumbersElement) {
        const lines = text.split('\n');
        const fragment = document.createDocumentFragment();
        
        lines.forEach((_, index) => {
            const div = document.createElement('div');
            div.textContent = index + 1;
            fragment.appendChild(div);
        });
        
        lineNumbersElement.innerHTML = '';
        lineNumbersElement.appendChild(fragment);
    },
    
    // 同步滚动
    syncScroll(source, target, sourceDisplay, targetDisplay) {
        target.scrollTop = source.scrollTop;
        target.scrollLeft = source.scrollLeft;
        sourceDisplay.scrollTop = source.scrollTop;
        sourceDisplay.scrollLeft = source.scrollLeft;
        targetDisplay.scrollTop = source.scrollTop;
        targetDisplay.scrollLeft = source.scrollLeft;
    }
};

// 差异对比管理
const diffManager = {
    // 计算差异
    compute(text1, text2) {
        const diffs = dmp.diff_main(text1, text2);
        dmp.diff_cleanupSemantic(diffs);
        return diffs;
    },
    
    // 显示差异
    display(diffs) {
        let leftText = '';
        let rightText = '';
        
        for (const diff of diffs) {
            const [operation, text] = [diff[0], diff[1]];
            const escapedText = utils.escapeHTML(text);
            
            switch (operation) {
                case -1: // 删除
                    leftText += `<span class="char-removed">${escapedText}</span>`;
                    break;
                case 1: // 添加
                    rightText += `<span class="char-added">${escapedText}</span>`;
                    break;
                case 0: // 相同
                    leftText += escapedText;
                    rightText += escapedText;
                    break;
            }
        }
        
        elements.leftDiffDisplay.innerHTML = leftText;
        elements.rightDiffDisplay.innerHTML = rightText;
        
        // 显示差异区域
        elements.leftDiffDisplay.style.visibility = 'visible';
        elements.rightDiffDisplay.style.visibility = 'visible';
    },
    
    // 隐藏差异显示
    hide() {
        elements.leftDiffDisplay.style.visibility = 'hidden';
        elements.rightDiffDisplay.style.visibility = 'hidden';
        elements.leftDiffDisplay.innerHTML = '';
        elements.rightDiffDisplay.innerHTML = '';
    },
    
    // 更新差异
    update() {
        const leftText = elements.leftEditor.value;
        const rightText = elements.rightEditor.value;
        
        // 如果两边都为空，隐藏差异显示
        if (!leftText.trim() && !rightText.trim()) {
            this.hide();
            return;
        }
        
        // JSON 模式处理
        if (state.isJsonMode) {
            const isLeftValid = utils.isValidJSON(leftText);
            const isRightValid = utils.isValidJSON(rightText);
            
            // 检查 JSON 有效性
            if (!isLeftValid && leftText.trim()) {
                notifications.show('左侧不是有效的 JSON 格式', true);
                return;
            }
            if (!isRightValid && rightText.trim()) {
                notifications.show('右侧不是有效的 JSON 格式', true);
                return;
            }
            
            // 格式化 JSON
            if (isLeftValid) {
                elements.leftEditor.value = utils.formatJSON(leftText);
            }
            if (isRightValid) {
                elements.rightEditor.value = utils.formatJSON(rightText);
            }
        }
        
        // 更新行号
        editor.updateLineNumbers(elements.leftEditor.value, elements.leftLineNumbers);
        editor.updateLineNumbers(elements.rightEditor.value, elements.rightLineNumbers);
        
        // 计算并显示差异
        const diffs = this.compute(elements.leftEditor.value, elements.rightEditor.value);
        this.display(diffs);
    }
};

// 事件处理
const eventHandlers = {
    // 处理输入
    handleInput: utils.debounce(() => {
        diffManager.update();
    }, 300),
    
    // 处理滚动
    handleScroll(e) {
        const source = e.target;
        const target = source === elements.leftEditor ? elements.rightEditor : elements.leftEditor;
        const sourceDisplay = source === elements.leftEditor ? elements.leftDiffDisplay : elements.rightDiffDisplay;
        const targetDisplay = source === elements.leftEditor ? elements.rightDiffDisplay : elements.leftDiffDisplay;
        
        editor.syncScroll(source, target, sourceDisplay, targetDisplay);
    },
    
    // 清除所有内容
    handleClear() {
        elements.leftEditor.value = '';
        elements.rightEditor.value = '';
        diffManager.hide();
        editor.updateLineNumbers('', elements.leftLineNumbers);
        editor.updateLineNumbers('', elements.rightLineNumbers);
        notifications.show('已清除所有内容');
    },
    
    // 交换内容
    handleSwap() {
        const temp = elements.leftEditor.value;
        elements.leftEditor.value = elements.rightEditor.value;
        elements.rightEditor.value = temp;
        
        // 添加动画效果
        elements.swapIcon.classList.add('rotating');
        setTimeout(() => {
            elements.swapIcon.classList.remove('rotating');
        }, 500);
        
        diffManager.update();
        notifications.show('已交换两侧内容');
    },
    
    // 切换 JSON 模式
    handleJsonModeToggle(e) {
        state.isJsonMode = e.target.checked;
        if (state.isJsonMode) {
            notifications.show('已启用 JSON 模式，输入的 JSON 将自动格式化');
            diffManager.update();
        } else {
            notifications.show('已关闭 JSON 模式');
        }
    },
    
    // 处理快捷键
    handleKeydown(e) {
        // Ctrl+Alt+X 交换内容
        if (e.ctrlKey && e.altKey && e.key === 'x') {
            e.preventDefault();
            this.handleSwap();
        }
        
        // Ctrl+Alt+J 切换 JSON 模式
        if (e.ctrlKey && e.altKey && e.key === 'j') {
            e.preventDefault();
            elements.jsonModeToggle.checked = !elements.jsonModeToggle.checked;
            this.handleJsonModeToggle({ target: elements.jsonModeToggle });
        }
        
        // Ctrl+Alt+C 清除内容
        if (e.ctrlKey && e.altKey && e.key === 'c') {
            e.preventDefault();
            this.handleClear();
        }
    }
};

// 初始化
function init() {
    // 初始化DOM元素
    elements = {
        leftEditor: document.getElementById('leftEditor'),
        rightEditor: document.getElementById('rightEditor'),
        leftDiffDisplay: document.getElementById('leftDiffDisplay'),
        rightDiffDisplay: document.getElementById('rightDiffDisplay'),
        leftLineNumbers: document.getElementById('leftLineNumbers'),
        rightLineNumbers: document.getElementById('rightLineNumbers'),
        jsonModeToggle: document.getElementById('jsonMode'),
        clearButton: document.getElementById('clearButton'),
        swapIcon: document.getElementById('swapIcon')
    };

    // 检查必要的 DOM 元素
    for (const [key, element] of Object.entries(elements)) {
        if (!element) {
            console.error(`无法找到元素: ${key}`);
            return;
        }
    }
    
    // 添加事件监听器
    elements.leftEditor.addEventListener('input', eventHandlers.handleInput);
    elements.rightEditor.addEventListener('input', eventHandlers.handleInput);
    elements.leftEditor.addEventListener('scroll', eventHandlers.handleScroll);
    elements.rightEditor.addEventListener('scroll', eventHandlers.handleScroll);
    elements.clearButton.addEventListener('click', eventHandlers.handleClear);
    elements.swapIcon.addEventListener('click', eventHandlers.handleSwap);
    elements.jsonModeToggle.addEventListener('change', eventHandlers.handleJsonModeToggle);
    document.addEventListener('keydown', eventHandlers.handleKeydown.bind(eventHandlers));
    
    // 监听编辑器大小变化
    new ResizeObserver(() => {
        editor.updateLineNumbers(elements.leftEditor.value, elements.leftLineNumbers);
    }).observe(elements.leftEditor);
    
    new ResizeObserver(() => {
        editor.updateLineNumbers(elements.rightEditor.value, elements.rightLineNumbers);
    }).observe(elements.rightEditor);
    
    // 显示初始提示
    setTimeout(() => {
        notifications.show('在两侧输入要对比的文本，系统将自动实时对比', false, 3000);
    }, 500);
    
    setTimeout(() => {
        notifications.show('快捷键：Ctrl+Alt+X 交换内容，Ctrl+Alt+J 切换 JSON 模式，Ctrl+Alt+C 清除内容', false, 5000);
    }, 3500);
}

// 等待DOM加载完成后再初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
} 