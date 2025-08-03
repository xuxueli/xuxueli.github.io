 // 密码生成器类
class PasswordGenerator {
    constructor() {
        // DOM元素
        this.lengthSlider = document.getElementById('lengthSlider');
        this.lengthValue = document.getElementById('lengthValue');
        this.lowercaseCheck = document.getElementById('lowercaseCheck');
        this.uppercaseCheck = document.getElementById('uppercaseCheck');
        this.numbersCheck = document.getElementById('numbersCheck');
        this.symbolsCheck = document.getElementById('symbolsCheck');
        this.passwordOutput = document.getElementById('passwordOutput');
        this.generateButton = document.getElementById('generateButton');
        this.copyButton = document.getElementById('copyButton');
        this.strengthIndicator = document.getElementById('strengthIndicator');
        this.strengthText = document.getElementById('strengthText');

        // 字符集
        this.charset = {
            lowercase: 'abcdefghijklmnopqrstuvwxyz',
            uppercase: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
            numbers: '0123456789',
            symbols: '!@#$%^&*()_+-=[]{}|;:,.<>?'
        };

        // 绑定事件
        this.bindEvents();
        
        // 生成初始密码
        this.generatePassword();
    }

    // 绑定事件
    bindEvents() {
        // 更新密码长度显示
        this.lengthSlider.addEventListener('input', () => {
            this.lengthValue.textContent = this.lengthSlider.value;
            this.generatePassword();
        });

        // 选项变化时重新生成密码
        [this.lowercaseCheck, this.uppercaseCheck, this.numbersCheck, this.symbolsCheck]
            .forEach(checkbox => {
                checkbox.addEventListener('change', () => this.generatePassword());
            });

        // 生成按钮点击事件
        this.generateButton.addEventListener('click', () => this.generatePassword());

        // 复制按钮点击事件
        this.copyButton.addEventListener('click', () => this.copyPassword());
    }

    // 生成密码
    generatePassword() {
        // 获取选中的字符集
        let charset = '';
        if (this.lowercaseCheck.checked) charset += this.charset.lowercase;
        if (this.uppercaseCheck.checked) charset += this.charset.uppercase;
        if (this.numbersCheck.checked) charset += this.charset.numbers;
        if (this.symbolsCheck.checked) charset += this.charset.symbols;

        // 如果没有选择任何字符集，使用小写字母作为默认值
        if (!charset) {
            charset = this.charset.lowercase;
            this.lowercaseCheck.checked = true;
        }

        // 生成密码
        const length = parseInt(this.lengthSlider.value);
        let password = '';
        const array = new Uint32Array(length);
        crypto.getRandomValues(array);
        
        for (let i = 0; i < length; i++) {
            password += charset[array[i] % charset.length];
        }

        // 确保密码包含所有选中的字符类型
        if (!this.validatePassword(password)) {
            this.generatePassword();
            return;
        }

        // 更新密码显示
        this.passwordOutput.value = password;

        // 评估密码强度
        this.evaluatePasswordStrength(password);
    }

    // 验证密码是否包含所有选中的字符类型
    validatePassword(password) {
        const hasLowercase = password.match(/[a-z]/);
        const hasUppercase = password.match(/[A-Z]/);
        const hasNumbers = password.match(/[0-9]/);
        const hasSymbols = password.match(/[!@#$%^&*()_+\-=\[\]{}|;:,.<>?]/);

        if (this.lowercaseCheck.checked && !hasLowercase) return false;
        if (this.uppercaseCheck.checked && !hasUppercase) return false;
        if (this.numbersCheck.checked && !hasNumbers) return false;
        if (this.symbolsCheck.checked && !hasSymbols) return false;

        return true;
    }

    // 评估密码强度
    evaluatePasswordStrength(password) {
        let score = 0;
        const length = password.length;

        // 长度评分
        if (length >= 8) score += 1;
        if (length >= 12) score += 1;
        if (length >= 16) score += 1;

        // 字符类型评分
        if (password.match(/[a-z]/)) score += 1;
        if (password.match(/[A-Z]/)) score += 1;
        if (password.match(/[0-9]/)) score += 1;
        if (password.match(/[!@#$%^&*()_+\-=\[\]{}|;:,.<>?]/)) score += 1;

        // 更新强度显示
        const strengthClasses = ['very-weak', 'weak', 'medium', 'strong', 'very-strong'];
        const strengthTexts = ['非常弱', '弱', '中等', '强', '非常强'];
        const strengthIndex = Math.min(Math.floor(score / 2), 4);

        this.strengthIndicator.className = 'strength-indicator ' + strengthClasses[strengthIndex];
        this.strengthText.textContent = '密码强度: ' + strengthTexts[strengthIndex];
    }

    // 复制密码
    async copyPassword() {
        try {
            await navigator.clipboard.writeText(this.passwordOutput.value);
            this.showCopyFeedback(true);
        } catch (err) {
            this.showCopyFeedback(false);
        }
    }

    // 显示复制反馈
    showCopyFeedback(success) {
        const originalText = this.copyButton.textContent;
        this.copyButton.textContent = success ? '已复制' : '复制失败';
        this.copyButton.style.backgroundColor = success ? 'var(--success-color)' : 'var(--danger-color)';

        setTimeout(() => {
            this.copyButton.innerHTML = `
                <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg">
                    <path d="M672 832H224c-52.928 0-96-43.072-96-96V160c0-52.928 43.072-96 96-96h448c52.928 0 96 43.072 96 96v576c0 52.928-43.072 96-96 96zM224 128c-17.632 0-32 14.368-32 32v576c0 17.632 14.368 32 32 32h448c17.632 0 32-14.368 32-32V160c0-17.632-14.368-32-32-32H224z" fill="currentColor"></path>
                    <path d="M800 960H320c-17.664 0-32-14.336-32-32s14.336-32 32-32h480c17.632 0 32-14.368 32-32V256c0-17.664 14.336-32 32-32s32 14.336 32 32v608c0 52.928-43.072 96-96 96z" fill="currentColor"></path>
                </svg>
                复制
            `;
            this.copyButton.style.backgroundColor = '';
        }, 2000);
    }
}

// 初始化密码生成器
document.addEventListener('DOMContentLoaded', () => {
    new PasswordGenerator();
});