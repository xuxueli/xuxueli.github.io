// DOM元素
const editor = document.getElementById('editor');
const notification = document.getElementById('notification');
const historyList = document.getElementById('historyList');
const lineNumbers = document.getElementById('lineNumbers');

// 初始化JSONEditor
const container = document.getElementById('jsoneditor');
const options = {
    mode: 'code',
    modes: ['code', 'tree'],
    onChangeText: function(jsonString) {
        // 当内容改变时，同步到原始编辑器并更新历史记录
        editor.value = jsonString;
        history.push(jsonString);
    },
    onValidationError: function(errors) {
        // 忽略验证错误，允许输入非JSON内容
    },
    mainMenuBar: false,
    statusBar: false,
    navigationBar: false
};
const jsonEditor = new JSONEditor(container, options);
// 设置空的初始值
jsonEditor.setText('');

// 历史记录管理
const history = {
    records: [],
    currentIndex: -1,
    maxSize: 50,

    push(value) {
        // 如果在历史记录中间位置做了修改，删除后面的记录
        if (this.currentIndex < this.records.length - 1) {
            this.records.splice(this.currentIndex + 1);
        }
        
        // 添加新记录
        this.records.push(value);
        
        // 如果超出最大大小，删除最早的记录
        if (this.records.length > this.maxSize) {
            this.records.shift();
        }
        
        this.currentIndex = this.records.length - 1;
    },

    undo() {
        if (this.currentIndex > 0) {
            this.currentIndex--;
            return this.records[this.currentIndex];
        }
        return null;
    },

    redo() {
        if (this.currentIndex < this.records.length - 1) {
            this.currentIndex++;
            return this.records[this.currentIndex];
        }
        return null;
    }
};

// JSON序列化历史记录管理
const jsonHistory = {
    maxSize: 20,
    storageKey: 'jsonToolHistory',

    // 获取历史记录
    getHistory() {
        const stored = localStorage.getItem(this.storageKey);
        return stored ? JSON.parse(stored) : [];
    },

    // 保存历史记录
    saveHistory(history) {
        localStorage.setItem(this.storageKey, JSON.stringify(history));
    },

    // 添加新记录
    addRecord(text) {
        const history = this.getHistory();
        const record = {
            text: text,
            preview: text.length > 100 ? text.substring(0, 100) + '...' : text,
            timestamp: new Date().toISOString()
        };

        history.unshift(record);
        if (history.length > this.maxSize) {
            history.pop();
        }

        this.saveHistory(history);
        this.renderHistory();
    },

    // 清空历史记录
    clearHistory() {
        localStorage.removeItem(this.storageKey);
        this.renderHistory();
    },

    // 渲染历史记录
    renderHistory() {
        const history = this.getHistory();
        historyList.innerHTML = '';

        history.forEach(record => {
            const item = document.createElement('div');
            item.className = 'history-item';
            item.innerHTML = `
                <div class="history-item-time">${new Date(record.timestamp).toLocaleString()}</div>
                <div class="history-item-preview">${record.preview}</div>
            `;
            item.addEventListener('click', () => {
                setEditorContent(record.text);
                showNotification('已恢复历史记录内容！');
            });
            historyList.appendChild(item);
        });
    }
};

// 初始化历史记录
history.push(editor.value);
jsonHistory.renderHistory();

// 更新行号
function updateLineNumbers() {
    const lines = editor.value.split('\n');
    const fragment = document.createDocumentFragment();
    
    lines.forEach((_, index) => {
        const div = document.createElement('div');
        div.textContent = index + 1;
        fragment.appendChild(div);
    });
    
    lineNumbers.innerHTML = '';
    lineNumbers.appendChild(fragment);
}

// 同步滚动
editor.addEventListener('scroll', () => {
    lineNumbers.scrollTop = editor.scrollTop;
});

// 监听文本变化
editor.addEventListener('input', () => {
    history.push(editor.value);
    updateLineNumbers();
});

// 初始化行号
updateLineNumbers();

// 监听键盘事件
editor.addEventListener('keydown', (e) => {
    // 检测操作系统
    const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
    
    // Mac: Cmd+Z, Win: Ctrl+Z
    if ((isMac ? e.metaKey : e.ctrlKey) && e.key === 'z' && !e.shiftKey) {
        e.preventDefault();
        const previousValue = history.undo();
        if (previousValue !== null) {
            editor.value = previousValue;
        }
    }
    
    // Mac: Cmd+Shift+Z, Win: Ctrl+Y
    if ((isMac && e.metaKey && e.shiftKey && e.key === 'z') || 
        (!isMac && e.ctrlKey && e.key === 'y')) {
        e.preventDefault();
        const nextValue = history.redo();
        if (nextValue !== null) {
            editor.value = nextValue;
        }
    }
});

// 工具函数：显示通知
function showNotification(message, isError = false) {
    notification.textContent = message;
    notification.className = `notification show${isError ? ' error' : ''}`;
    setTimeout(() => {
        notification.className = 'notification';
    }, 3000);
}

// 工具函数：获取选中的文本或全部文本
function getSelectedText() {
    const selectedText = editor.value.substring(editor.selectionStart, editor.selectionEnd);
    return selectedText || editor.value;
}

// 工具函数：替换选中的文本或全部文本
function replaceSelectedText(newText) {
    const start = editor.selectionStart;
    const end = editor.selectionEnd;
    const hasSelection = start !== end;

    if (hasSelection) {
        const before = editor.value.substring(0, start);
        const after = editor.value.substring(end);
        editor.value = before + newText + after;
        // 保持选区
        editor.setSelectionRange(start, start + newText.length);
    } else {
        editor.value = newText;
        // 将光标移到末尾
        editor.setSelectionRange(newText.length, newText.length);
    }
    
    // 添加到历史记录
    history.push(editor.value);
}

// 工具函数：尝试去除转义
function tryUnescapeText(text) {
    try {
        return text
            .replace(/\\"/g, '"')     // 双引号
            .replace(/\\'/g, "'")     // 单引号
            .replace(/\\f/g, '\f')    // 换页
            .replace(/\\\\/g, '\\');  // 反斜杠（最后处理）
    } catch {
        return text;
    }
}

// 工具函数：获取编辑器内容
function getEditorContent() {
    try {
        return jsonEditor.getText();
    } catch (error) {
        return '';
    }
}

// 工具函数：设置编辑器内容
function setEditorContent(text) {
    try {
        // 尝试解析JSON
        const json = JSON.parse(text);
        jsonEditor.set(json);
    } catch {
        // 如果不是有效的JSON，直接设置文本
        jsonEditor.setText(text);
    }
    // 同步到原始编辑器
    editor.value = text;
    // 添加到历史记录
    history.push(text);
}

// JSON序列化
document.getElementById('jsonSerialize').addEventListener('click', () => {
    try {
        const text = getEditorContent();
        let result;

        // 如果文本为空，直接返回
        if (!text || !text.trim()) {
            showNotification('请输入要处理的文本', true);
            return;
        }
        
        try {
            // 尝试先去除转义
            const unescapedText = tryUnescapeText(text);
            // 尝试解析JSON
            const parsed = JSON.parse(unescapedText);
            // 格式化JSON，使用2个空格缩进
            result = JSON.stringify(parsed, null, 2);
        } catch (error)  {
            try {
                // 如果去除转义后解析失败，尝试直接解析原文本
                const parsed = JSON.parse(text);
                result = JSON.stringify(parsed, null, 2);
            } catch (error2)  {
                // 如果都不是有效的JSON，才当作普通文本序列化
                /*result = JSON.stringify(text);*/

                showNotification('JSON序列化失败：' + error2, true);
                return;
            }
        }
        
        setEditorContent(result);
        // 添加到JSON序列化历史记录
        jsonHistory.addRecord(result);
        showNotification('JSON序列化成功！');
    } catch (error) {
        showNotification('JSON序列化失败：' + error.message, true);
    }
});

// 压缩JSON
document.getElementById('jsonCompress').addEventListener('click', () => {
    try {
        const text = getEditorContent();
        console.log('原始文本:', text); // 调试日志

        // 如果文本为空，直接返回
        if (!text || !text.trim()) {
            showNotification('请输入要处理的文本', true);
            return;
        }

        let result;
        try {
            // 尝试直接解析JSON
            const parsed = JSON.parse(text);
            console.log('解析后的JSON:', parsed); // 调试日志
            result = JSON.stringify(parsed);
            console.log('压缩后的JSON:', result); // 调试日志
        } catch (parseError) {
            console.log('直接解析失败:', parseError); // 调试日志
            try {
                // 尝试去除转义后再解析
                const unescapedText = tryUnescapeText(text);
                console.log('去除转义后的文本:', unescapedText); // 调试日志
                const parsed = JSON.parse(unescapedText);
                result = JSON.stringify(parsed);
            } catch (unescapeError) {
                console.log('去除转义后解析失败:', unescapeError); // 调试日志
                showNotification('请输入有效的JSON文本', true);
                return;
            }
        }

        // 确保result不为空
        if (!result) {
            showNotification('压缩失败：无法生成结果', true);
            return;
        }

        // 移除所有空格、换行和缩进
        result = result.replace(/\s+/g, '');
        
        // 设置编辑器内容
        jsonEditor.setText(result);
        // 同步到原始编辑器
        editor.value = result;
        // 添加到历史记录
        history.push(result);
        
        showNotification('JSON压缩成功！');
    } catch (error) {
        console.error('压缩过程出错:', error); // 调试日志
        showNotification('JSON压缩失败：' + error.message, true);
    }
});

// 工具函数：获取jsonEditor中选中的文本
function getJsonEditorSelection() {
    try {
        const aceEditor = jsonEditor.aceEditor;
        if (!aceEditor) return null;
        
        const selection = aceEditor.getSelection();
        const range = selection.getRange();
        const selectedText = aceEditor.session.getTextRange(range);
        
        return {
            text: selectedText,
            hasSelection: selectedText.length > 0,
            range: range
        };
    } catch (error) {
        console.error('获取选中文本失败:', error);
        return null;
    }
}

// 工具函数：替换jsonEditor中选中的文本
function replaceJsonEditorSelection(newText, selection) {
    try {
        const aceEditor = jsonEditor.aceEditor;
        if (!aceEditor) return false;
        
        if (selection && selection.hasSelection) {
            aceEditor.session.replace(selection.range, newText);
        } else {
            jsonEditor.setText(newText);
        }
        
        // 同步到原始编辑器
        editor.value = jsonEditor.getText();
        // 添加到历史记录
        history.push(editor.value);
        
        return true;
    } catch (error) {
        console.error('替换选中文本失败:', error);
        return false;
    }
}

// 转义文本
document.getElementById('escapeText').addEventListener('click', () => {
    try {
        // 获取选中的文本
        const selection = getJsonEditorSelection();
        if (!selection) {
            showNotification('获取选中文本失败', true);
            return;
        }

        // 获取要处理的文本
        const textToEscape = selection.hasSelection ? selection.text : jsonEditor.getText();
        
        // 处理文本
        const result = textToEscape
            .replace(/\\/g, '\\\\')  // 反斜杠
            .replace(/"/g, '\\"')    // 双引号
            .replace(/'/g, "\\'")    // 单引号
            .replace(/\f/g, '\\f');  // 换页

        // 替换文本
        if (replaceJsonEditorSelection(result, selection)) {
            showNotification('文本转义成功！');
        } else {
            showNotification('文本转义失败', true);
        }
    } catch (error) {
        showNotification('文本转义失败：' + error.message, true);
    }
});

// 去除转义
document.getElementById('unescapeText').addEventListener('click', () => {
    try {
        // 获取选中的文本
        const selection = getJsonEditorSelection();
        if (!selection) {
            showNotification('获取选中文本失败', true);
            return;
        }

        // 获取要处理的文本
        const textToUnescape = selection.hasSelection ? selection.text : jsonEditor.getText();
        
        // 处理文本
        const result = tryUnescapeText(textToUnescape);

        // 替换文本
        if (replaceJsonEditorSelection(result, selection)) {
            showNotification('去除转义成功！');
        } else {
            showNotification('去除转义失败', true);
        }
    } catch (error) {
        showNotification('去除转义失败：' + error.message, true);
    }
});

// 清空文本
document.getElementById('clearText').addEventListener('click', () => {
    setEditorContent('');
    showNotification('已清空文本！');
});

// 清空历史记录
document.getElementById('clearHistory').addEventListener('click', () => {
    jsonHistory.clearHistory();
    showNotification('已清空历史记录！');
});

// 复制文本
document.getElementById('copyText').addEventListener('click', () => {
    const text = getEditorContent();
    if (!text) {
        showNotification('没有可复制的内容！', true);
        return;
    }

    // 尝试使用现代Clipboard API
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => {
            showNotification('已复制到剪贴板！');
        }).catch(error => {
            fallbackCopy(text);
        });
    } else {
        fallbackCopy(text);
    }
});

// 添加兼容性复制函数
function fallbackCopy(text) {
    try {
        // 创建临时textarea元素
        const textArea = document.createElement('textarea');
        textArea.value = text;
        
        // 确保textarea在视口之外
        textArea.style.position = 'fixed';
        textArea.style.left = '-9999px';
        textArea.style.top = '0';
        
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();

        // 尝试使用execCommand
        const successful = document.execCommand('copy');
        document.body.removeChild(textArea);

        if (successful) {
            showNotification('已复制到剪贴板！');
        } else {
            showNotification('复制失败：您的浏览器不支持自动复制，请手动复制', true);
        }
    } catch (err) {
        showNotification('复制失败：' + err.message, true);
    }
}