/**
 * Cron表达式生成器 V5
 * 支持7位长度的cron表达式：秒 分 时 日 月 周 年
 */

document.addEventListener('DOMContentLoaded', () => {
  // 常量定义
  const FIELDS = ['second', 'minute', 'hour', 'day', 'month', 'week', 'year'];
  const DEFAULT_EXPRESSION = '* * * * * ? *';

  // DOM元素
  const cronExpression = document.getElementById('cron-expression');
  const errorMessage = document.querySelector('.error-message');
  const nextExecutions = document.getElementById('next-executions');
  const timeUnitPanels = document.querySelectorAll('.time-unit-panel');
  const presetButtons = document.querySelectorAll('.preset-btn');

  // 初始状态
  cronExpression.value = DEFAULT_EXPRESSION;
  
  /**
   * 初始化函数
   */
  function init() {
    // 初始化事件监听
    initEventListeners();
    
    // 设置面板初始状态
    updatePanelsFromExpression(DEFAULT_EXPRESSION);
    
    // 更新执行时间预览
    updateExecutionTimes(DEFAULT_EXPRESSION);
  }

  /**
   * 初始化事件监听
   */
  function initEventListeners() {
    // Cron表达式输入框变化事件
    cronExpression.addEventListener('input', handleExpressionInput);
    
    // 单选按钮变化事件
    timeUnitPanels.forEach(panel => {
      const field = panel.dataset.field;
      
      // 获取该时间单位的所有单选按钮
      const radioButtons = panel.querySelectorAll(`input[name="${field}-type"]`);
      
      // 绑定变化事件
      radioButtons.forEach(radio => {
        radio.addEventListener('change', () => {
          updateSelectorVisibility(field);
          generateCronExpression();
        });
      });
      
      // 指定值选择器中的选项点击事件
      const specificSelector = panel.querySelector(`#${field}-specific-selector`);
      if (specificSelector) {
        const selectorItems = specificSelector.querySelectorAll('.selector-item');
        selectorItems.forEach(item => {
          item.addEventListener('click', () => {
            item.classList.toggle('selected');
            generateCronExpression();
          });
        });
      }
      
      // 区间选择器的输入框变化事件
      const rangeInputs = panel.querySelectorAll(`#${field}-range-selector input, #${field}-range-selector select`);
      rangeInputs.forEach(input => {
        input.addEventListener('change', generateCronExpression);
        input.addEventListener('input', generateCronExpression);
      });
      
      // 周期选择器的输入框变化事件
      const cycleInputs = panel.querySelectorAll(`#${field}-cycle-selector input, #${field}-cycle-selector select`);
      cycleInputs.forEach(input => {
        input.addEventListener('change', generateCronExpression);
        input.addEventListener('input', generateCronExpression);
      });
    });
    
    // 常用表达式点击事件
    presetButtons.forEach(button => {
      button.addEventListener('click', () => {
        const expression = button.dataset.expression;
        cronExpression.value = expression;
        updatePanelsFromExpression(expression);
        updateExecutionTimes(expression);
      });
    });
  }

  /**
   * 处理表达式输入变化
   */
  function handleExpressionInput() {
    const expression = cronExpression.value.trim();
    
    // 验证表达式
    if (isValidCronExpression(expression)) {
      errorMessage.textContent = '';
      updatePanelsFromExpression(expression);
      updateExecutionTimes(expression);
    } else {
      errorMessage.textContent = '无效的Cron表达式';
    }
  }

  /**
   * 验证Cron表达式是否有效
   */
  function isValidCronExpression(expression) {
    // 简单验证：7个字段，由空格分隔
    const parts = expression.split(' ');
    if (parts.length !== 7) return false;
    
    // TODO: 可以添加更复杂的验证逻辑
    
    return true;
  }

  /**
   * 根据表达式更新配置面板
   */
  function updatePanelsFromExpression(expression) {
    const parts = expression.split(' ');
    if (parts.length !== 7) return;
    
    // 更新各字段
    FIELDS.forEach((field, index) => {
      const value = parts[index];
      updateFieldPanel(field, value);
    });
  }

  /**
   * 更新单个字段的面板
   */
  function updateFieldPanel(field, value) {
    // 获取该字段的面板
    const panel = document.querySelector(`.time-unit-panel[data-field="${field}"]`);
    if (!panel) return;
    
    // 根据值类型选择合适的单选按钮
    if (value === '*') {
      // 每个时间单位
      selectRadioButton(field, 'every');
    } else if (value === '?') {
      // 不指定
      selectRadioButton(field, 'not-specify');
    } else if (/^\d+$/.test(value) || /^\d+(,\d+)*$/.test(value)) {
      // 指定值
      selectRadioButton(field, 'specific');
      updateSpecificSelector(field, value.split(','));
    } else if (/^\d+-\d+$/.test(value)) {
      // 范围
      selectRadioButton(field, 'range');
      const [start, end] = value.split('-');
      updateRangeSelector(field, start, end);
    } else if (/^(\d+|\*)\/\d+$/.test(value)) {
      // 周期
      selectRadioButton(field, 'cycle');
      let [start, step] = value.split('/');
      if (start === '*') start = '0';
      updateCycleSelector(field, start, step);
    }
    
    // 更新选择器可见性
    updateSelectorVisibility(field);
  }

  /**
   * 选择单选按钮
   */
  function selectRadioButton(field, type) {
    const radio = document.getElementById(`${field}-${type}`);
    if (radio) radio.checked = true;
  }

  /**
   * 更新指定值选择器
   */
  function updateSpecificSelector(field, values) {
    const selector = document.querySelector(`#${field}-specific-selector`);
    if (!selector) return;
    
    // 清除所有选中状态
    const items = selector.querySelectorAll('.selector-item');
    items.forEach(item => item.classList.remove('selected'));
    
    // 选中指定的值
    values.forEach(val => {
      const item = selector.querySelector(`.selector-item[data-value="${val}"]`);
      if (item) item.classList.add('selected');
    });
  }

  /**
   * 更新范围选择器
   */
  function updateRangeSelector(field, start, end) {
    const startInput = document.querySelector(`.${field}-range-start`);
    const endInput = document.querySelector(`.${field}-range-end`);
    
    if (startInput) startInput.value = start;
    if (endInput) endInput.value = end;
  }

  /**
   * 更新周期选择器
   */
  function updateCycleSelector(field, start, step) {
    const startInput = document.querySelector(`.${field}-cycle-start`);
    const stepInput = document.querySelector(`.${field}-cycle-step`);
    
    if (startInput) startInput.value = start;
    if (stepInput) stepInput.value = step;
  }

  /**
   * 更新选择器可见性
   */
  function updateSelectorVisibility(field) {
    const panel = document.querySelector(`.time-unit-panel[data-field="${field}"]`);
    if (!panel) return;
    
    // 获取选中的类型
    const selectedType = panel.querySelector(`input[name="${field}-type"]:checked`);
    if (!selectedType) return;
    
    // 获取所有值选择器
    const valueSelectors = panel.querySelectorAll('.value-selector');
    valueSelectors.forEach(selector => {
      selector.classList.remove('active');
    });
    
    const type = selectedType.value;
    
    // 根据类型显示相应的选择器
    switch (type) {
      case 'specific':
        const specificSelector = panel.querySelector(`#${field}-specific-selector`);
        if (specificSelector) {
          specificSelector.classList.add('active');
        }
        break;
      case 'range':
        const rangeSelector = panel.querySelector(`#${field}-range-selector`);
        if (rangeSelector) {
          rangeSelector.classList.add('active');
        }
        break;
      case 'cycle':
        const cycleSelector = panel.querySelector(`#${field}-cycle-selector`);
        if (cycleSelector) {
          cycleSelector.classList.add('active');
        }
        break;
    }
  }

  /**
   * 生成Cron表达式
   */
  function generateCronExpression() {
    const parts = [];
    
    // 解析各个字段的值
    FIELDS.forEach(field => {
      parts.push(getFieldValue(field));
    });
    
    // 组合为Cron表达式
    const expression = parts.join(' ');
    
    // 更新输入框和执行时间预览
    cronExpression.value = expression;
    updateExecutionTimes(expression);
  }

  /**
   * 获取字段值
   */
  function getFieldValue(field) {
    // 获取当前选中的类型
    const selectedType = document.querySelector(`input[name="${field}-type"]:checked`).value;
    
    // 根据类型获取值
    switch(selectedType) {
      case 'every':
        return '*';
      case 'not-specify':
        return '?';
      case 'specific':
        return getSpecificValue(field);
      case 'range':
        return getRangeValue(field);
      case 'cycle':
        return getCycleValue(field);
      default:
        return '*';
    }
  }

  /**
   * 获取指定值
   */
  function getSpecificValue(field) {
    const selector = document.querySelector(`#${field}-specific-selector`);
    if (!selector) return '*';
    
    // 获取选中的项
    const selectedItems = selector.querySelectorAll('.selector-item.selected');
    if (selectedItems.length === 0) return '*';
    
    // 返回逗号分隔的值
    return Array.from(selectedItems)
      .map(item => item.dataset.value)
      .sort((a, b) => Number(a) - Number(b))
      .join(',');
  }

  /**
   * 获取范围值
   */
  function getRangeValue(field) {
    const startInput = document.querySelector(`.${field}-range-start`);
    const endInput = document.querySelector(`.${field}-range-end`);
    
    if (!startInput || !endInput) return '*';
    
    const start = startInput.value || '0';
    const end = endInput.value || '59';
    
    return `${start}-${end}`;
  }

  /**
   * 获取周期值
   */
  function getCycleValue(field) {
    const startInput = document.querySelector(`.${field}-cycle-start`);
    const stepInput = document.querySelector(`.${field}-cycle-step`);
    
    if (!startInput || !stepInput) return '*';
    
    const start = startInput.value || '0';
    const step = stepInput.value || '1';
    
    // 使用标准的格式：start/step
    return `${start}/${step}`;
  }

  /**
   * 更新执行时间预览
   */
  function updateExecutionTimes(expression) {
    // 清空预览区域，显示加载中
    nextExecutions.innerHTML = '<div class="loading">计算中...</div>';
    
    try {
      // 计算未来10次执行时间
      const execTimes = calculateNextExecutionTimes(expression, 10);
      
      // 更新UI
      nextExecutions.innerHTML = '';
      execTimes.forEach(time => {
        const item = document.createElement('div');
        item.className = 'execution-time'; // 添加特定类名，方便后续样式调整
        item.textContent = time;
        nextExecutions.appendChild(item);
      });
    } catch (error) {
      nextExecutions.innerHTML = `<div class="error">计算出错: ${error.message}</div>`;
    }
  }

  /**
   * 计算下次执行时间
   * 注意：这是一个简化实现，用于演示。实际应用中可能需要更复杂的逻辑。
   */
  function calculateNextExecutionTimes(expression, count) {
    // 强制限制count为10，避免生成过多执行时间
    count = Math.min(count, 10);
    
    const execTimes = [];
    const now = new Date();
    let date = new Date(now.getTime());
    
    // 解析表达式
    const parts = expression.split(' ');
    const cronParts = {
      second: parseCronPart(parts[0], 0, 59),
      minute: parseCronPart(parts[1], 0, 59),
      hour: parseCronPart(parts[2], 0, 23),
      day: parseCronPart(parts[3], 1, 31),
      month: parseCronPart(parts[4], 1, 12),
      week: parseCronPart(parts[5], 1, 7),
      year: parseCronPart(parts[6], now.getFullYear(), 2099)
    };
    
    // 处理日期和星期的互斥关系
    const useDay = parts[3] !== '?';
    const useWeek = parts[5] !== '?';
    
    // 优化：检查是否所有时间部分都是固定值
    const isFixedTimeOfDay = 
      cronParts.second.length === 1 && 
      cronParts.minute.length === 1 && 
      cronParts.hour.length === 1;
    
    // 对于固定时间表达式，先设置为当天的指定时间
    if (isFixedTimeOfDay) {
      // 设置为今天的指定时间点
      date.setHours(cronParts.hour[0], cronParts.minute[0], cronParts.second[0], 0);
      
      // 如果设置的时间早于当前时间，则前进到明天同一时间
      if (date <= now) {
        date.setDate(date.getDate() + 1);
      }
    }
    
    // 需要同时考虑日期和星期的话，找到满足两者条件的日期
    let attempts = 0;
    let maxAttempts = 10000; // 增加最大尝试次数
    
    for (let i = 0; i < count && attempts < maxAttempts; attempts++) {
      // 检查是否匹配所有部分
      if (
        matchesCronPart(date.getSeconds(), cronParts.second) &&
        matchesCronPart(date.getMinutes(), cronParts.minute) &&
        matchesCronPart(date.getHours(), cronParts.hour) &&
        (!useDay || matchesCronPart(date.getDate(), cronParts.day)) &&
        matchesCronPart(date.getMonth() + 1, cronParts.month) &&
        (!useWeek || matchesCronPart(date.getDay() === 0 ? 7 : date.getDay(), cronParts.week)) &&
        matchesCronPart(date.getFullYear(), cronParts.year)
      ) {
        // 找到匹配的执行时间，添加到结果中
        execTimes.push(formatDate(date));
        i++;
        
        // 计算下一个可能的执行时间
        if (isFixedTimeOfDay) {
          // 固定时间表达式：直接跳到下一天同一时间
          date.setDate(date.getDate() + 1);
        } else {
          // 非固定时间表达式：增加一秒
          date.setSeconds(date.getSeconds() + 1);
          
          // 智能跳转
          if (cronParts.second.length === 1 && cronParts.second[0] === 0) {
            // 如果秒固定为0，直接跳到下一分钟
            date.setSeconds(59);
          }
        }
      } else {
        // 当前时间不匹配，需要前进
        if (isFixedTimeOfDay) {
          // 对于固定时间表达式，检查是否是由于日期或星期不满足
          // 如果是，直接跳到下一天
          date.setDate(date.getDate() + 1);
          date.setHours(cronParts.hour[0], cronParts.minute[0], cronParts.second[0], 0);
        } else {
          // 默认情况，增加一秒
          date.setSeconds(date.getSeconds() + 1);
        }
      }
    }
    
    // 如果仍未找到足够的执行时间，添加一条说明信息
    if (execTimes.length === 0) {
      execTimes.push("未能找到匹配的执行时间，请检查表达式");
    } else if (execTimes.length < count) {
      execTimes.push("...");
    }
    
    return execTimes;
  }

  /**
   * 解析Cron表达式的每个部分
   */
  function parseCronPart(part, min, max) {
    if (part === '*' || part === '?') {
      // 返回所有可能的值
      const values = [];
      for (let i = min; i <= max; i++) {
        values.push(i);
      }
      return values;
    } else if (part.includes(',')) {
      // 多个指定值
      return part.split(',').map(v => parseInt(v, 10));
    } else if (part.includes('-')) {
      // 范围
      const [start, end] = part.split('-').map(v => parseInt(v, 10));
      const values = [];
      for (let i = start; i <= end; i++) {
        values.push(i);
      }
      return values;
    } else if (part.includes('/')) {
      // 周期
      let [start, step] = part.split('/');
      step = parseInt(step, 10);
      
      if (start === '*') {
        start = min;
      } else {
        start = parseInt(start, 10);
      }
      
      const values = [];
      for (let i = start; i <= max; i += step) {
        values.push(i);
      }
      return values;
    } else {
      // 单个值
      return [parseInt(part, 10)];
    }
  }

  /**
   * 检查值是否匹配Cron部分
   */
  function matchesCronPart(value, cronValues) {
    return cronValues.includes(value);
  }

  /**
   * 格式化日期
   */
  function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }

  // 初始化应用
  init();
}); 