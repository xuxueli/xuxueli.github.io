<h2 style="color:#4db6ac !important" >Vue3 教程</h2>
> 【原创】2026-08-07

[TOCM]

[TOC]


# 一、Vue3 简介

## 1.1 什么是 Vue

Vue 是一套用于构建用户界面的渐进式 JavaScript 框架。它采用"增量开发"的思想：可以只在一个页面中用 `<script>` 引入，也可以搭配 Vue Router、Pinia 等生态构成完整的单页应用（SPA）。

Vue 3.x 是当前主版本，于 2020 年 9 月正式发布，从底层做了重写，并提供了与 Vue 2 完全兼容的迁移方案。

## 1.2 Vue3 与 Vue2 的核心差异

| 维度 | Vue2 | Vue3 |
| --- | --- | --- |
| 响应式原理 | `Object.defineProperty`（需递归遍历，无法监听新增/删除属性） | `Proxy`（可监听新增/删除、数组索引等，性能更好） |
| API 风格 | 选项式 API（`data`/`methods`/`computed`） | 组合式 API（`setup` / `<script setup>`） |
| 逻辑复用 | Mixin（有命名冲突、来源不透明问题） | 组合式函数 `useXxx()` |
| 模板 | 单根节点 | 支持 Fragment（多根节点） |
| 类型支持 | 弱 | 全面使用 TypeScript 编写 |
| 性能 | 基线 | 更快（编译优化、PatchFlags、静态提升） |
| 虚拟 DOM | 全量 diff | Block Tree + 动态节点标记（`patchFlag`） |
| 异步组件 | 简单 `() => import()` | `defineAsyncComponent` + `<Suspense>` |
| 全局 API | `Vue.use` / `Vue.component` / `new Vue()` | `createApp()`，支持多实例 |

## 1.3 Vue3 生态版本对照

| 生态 | Vue2 版本 | Vue3 版本 |
| --- | --- | --- |
| 核心框架 | Vue 2.7 | Vue 3.x |
| 路由 | vue-router@3 | vue-router@4 |
| 状态管理 | Vuex@3 / Pinia | Pinia（官方推荐） |
| 脚手架 | vue-cli | Vite |
| UI 库 | Element UI / View UI | Element Plus / Naive UI / Ant Design Vue |
| 构建 | webpack | Vite（Rollup / esbuild） |

## 1.4 组合式 API 与选项式 API

- **选项式 API（Options API）**：Vue2 风格，按 `data`、`methods`、`computed`、`watch` 等"选项"组织代码。上手简单，但同一个业务逻辑的代码会被分散到各个选项中。
- **组合式 API（Composition API）**：Vue3 推荐风格，按"逻辑关注点"组织代码。相关代码集中在一起，配合 `<script setup>` 更简洁，逻辑复用靠自定义组合式函数。

Vue3 完全兼容选项式 API，二者可以混用，但新项目建议统一使用组合式 API。

# 二、快速上手

## 2.1 方式一：CDN 引入（最简单）

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8" />
  <title>Vue3 CDN Demo</title>
  <script src="https://unpkg.com/vue@3/dist/vue.global.prod.js"></script>
</head>
<body>
  <div id="app">
    <h1>{{ message }}</h1>
    <button @click="count++">count is {{ count }}</button>
  </div>

  <script>
    const { createApp, ref } = Vue

    createApp({
      setup() {
        const message = ref('Hello Vue3')
        const count = ref(0)
        return { message, count }
      }
    }).mount('#app')
  </script>
</body>
</html>
```

## 2.2 方式二：官方脚手架 create-vue

create-vue 是 Vue3 官方脚手架，基于 Vite：

```
# npm
npm create vue@latest

# pnpm
pnpm create vue@latest
```

交互式选项（可全部回车使用默认）：

```
✔ Project name: … my-vue-app
✔ Add TypeScript? … No / Yes
✔ Add JSX Support? … No / Yes
✔ Add Vue Router for Single Page Application? … No / Yes
✔ Add Pinia for state management? … No / Yes
✔ Add Vitest for Unit Testing? … No / Yes
✔ Add an End-to-End Testing Solution? … No / Yes
✔ Add ESLint for code quality? … No / Yes
```

进入项目并启动：

```
cd my-vue-app
npm install
npm run dev        # 开发服务器，默认 http://localhost:5173
npm run build      # 生产构建，输出 dist/
npm run preview    # 本地预览生产构建
```

## 2.3 项目目录结构

```
my-vue-app/
├── index.html              # 应用入口 HTML
├── package.json
├── vite.config.ts          # Vite 配置
├── tsconfig.json           # TypeScript 配置
├── public/                 # 静态资源（原样拷贝，不走打包）
├── src/
│   ├── main.ts             # 入口文件，创建并挂载应用
│   ├── App.vue             # 根组件
│   ├── assets/             # 需要打包处理的资源（图片、样式）
│   ├── components/         # 通用组件
│   ├── router/             # 路由配置
│   ├── stores/             # Pinia 状态
│   └── views/              # 页面级组件
└── .env.development        # 环境变量（可选）
```

## 2.4 入口文件与单文件组件

`src/main.ts`：

```ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())   // 挂载 Pinia
app.use(router)          // 挂载 Router
app.mount('#app')        // 挂载到 index.html 中的 #app
```

单文件组件（SFC）结构 —— 由三部分（块）组成：

```vue
<template>
  <div class="demo">{{ message }}</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const message = ref('Hello SFC')
</script>

<style scoped>
.demo {
  color: #4db6ac;
}
</style>
```

- `<template>`：模板，最终会被编译为虚拟 DOM 渲染函数。
- `<script setup>`：逻辑，`setup` 语法糖，所有顶层声明自动暴露给模板。
- `<style scoped>`：样式，`scoped` 表示仅对当前组件生效。

# 三、模板语法与指令

## 3.1 插值文本与原始 HTML

```vue
<template>
  <div>
    <!-- 文本插值，支持任意 JS 表达式 -->
    <p>{{ message }}</p>
    <p>{{ count + 1 }}</p>
    <p>{{ isOk ? 'YES' : 'NO' }}</p>
    <p>{{ message.split('').reverse().join('') }}</p>

    <!-- v-html：渲染真实 HTML（注意 XSS 风险，不要用于用户输入） -->
    <div v-html="rawHtml"></div>
  </div>
</template>

<script setup lang="ts">
const message = 'Hello'
const count = 1
const isOk = true
const rawHtml = '<strong style="color:red">bold</strong>'
</script>
```

## 3.2 v-bind 属性绑定

`v-bind` 简写为 `:`，用于动态绑定属性：

```vue
<template>
  <!-- 完整写法 -->
  <img v-bind:src="imgUrl" v-bind:alt="title" />
  <!-- 简写 -->
  <img :src="imgUrl" :alt="title" />
  <!-- 绑定布尔属性 -->
  <button :disabled="!canSubmit">提交</button>
  <!-- 绑定动态属性名 -->
  <div :[attrName]="value"></div>
  <!-- 对象形式一次性绑定多个 -->
  <div v-bind="{ id: 'box', 'data-code': code }"></div>
</template>
```

绑定 class 与 style：

```vue
<template>
  <!-- 数组语法 -->
  <div :class="['box', isActive && 'active', isError ? 'error' : '']"></div>
  <!-- 对象语法 -->
  <div :class="{ active: isActive, error: isError }"></div>

  <!-- style 对象语法 -->
  <div :style="{ color: textColor, fontSize: fontSize + 'px' }"></div>
  <!-- style 数组语法（后者覆盖前者） -->
  <div :style="[baseStyle, overrideStyle]"></div>
</template>
```

## 3.3 条件渲染 v-if / v-else-if / v-else / v-show

```vue
<template>
  <div v-if="type === 'a'">A</div>
  <div v-else-if="type === 'b'">B</div>
  <div v-else>其他</div>

  <!-- v-show：始终渲染，仅切换 display: none -->
  <div v-show="visible">不会销毁，只是隐藏</div>
</template>
```

`v-if` 与 `v-show` 的选择：

| 指令 | 机制 | 初始开销 | 切换开销 | 适用场景 |
| --- | --- | --- | --- | --- |
| v-if | 条件为假时元素不渲染 | 低（无节点） | 高（创建/销毁 DOM） | 很少切换，条件基本稳定 |
| v-show | 元素始终渲染，切换 display | 高（始终有节点） | 低 | 高频切换（如 Tab 页签） |

## 3.4 列表渲染 v-for

```vue
<template>
  <!-- 遍历数组 -->
  <ul>
    <li v-for="(item, index) in items" :key="item.id">
      {{ index }} - {{ item.name }}
    </li>
  </ul>

  <!-- 遍历对象 -->
  <p v-for="(value, key, index) in obj" :key="key">{{ key }}: {{ value }}</p>

  <!-- 遍历数字范围（1..10） -->
  <span v-for="n in 10" :key="n">{{ n }}</span>
</template>

<script setup lang="ts">
const items = [
  { id: 1, name: 'Vue' },
  { id: 2, name: 'React' },
  { id: 3, name: 'Svelte' }
]
const obj = { a: 1, b: 2, c: 3 }
</script>
```

**key 的重要性**：`key` 用于让 Vue 准确识别节点、复用和重排。列表有增删、排序或含输入框等有状态元素时，务必使用稳定的 `key`（建议使用唯一 id，不要用 index）。

`v-for` 与 `v-if` 同元素时的优先级（与 Vue2 不同）：

- Vue2：`v-for` 优先于 `v-if`（同元素时 `v-if` 无法访问 `v-for` 的变量，且会产生性能浪费）。
- Vue3：`v-if` 优先于 `v-for`，因此 `v-if` 中无法访问 `v-for` 的变量。

**推荐做法**：用 `template` 包裹分开写，或用计算属性过滤后再遍历：

```vue
<template>
  <!-- 不推荐：v-if 拿不到 item -->
  <!-- <li v-for="item in items" v-if="item.active">...</li> -->

  <!-- 推荐：分开写 -->
  <template v-for="item in activeItems" :key="item.id">
    <li>{{ item.name }}</li>
  </template>
</template>
<script setup lang="ts">
import { computed } from 'vue'
const items = [{ id: 1, name: 'A', active: true }, { id: 2, name: 'B', active: false }]
const activeItems = computed(() => items.filter(i => i.active))
</script>
```

## 3.5 事件处理 v-on

`v-on` 简写为 `@`：

```vue
<template>
  <!-- 内联表达式 -->
  <button @click="count++">+1</button>
  <!-- 方法名 -->
  <button @click="handleClick">点击</button>
  <!-- 传参并访问原生事件对象 $event -->
  <button @click="handleAdd(2, $event)">+2</button>
  <!-- 事件修饰符 -->
  <div @click.stop="onDivClick">阻止冒泡</div>
  <a @click.prevent="onLinkClick">阻止默认行为</a>
  <input @keyup.enter="onEnter" />                <!-- 键盘修饰符 -->
  <button @click.once="onOnce">只触发一次</button>
  <!-- 按键别名：.enter .tab .delete .esc .space .up .down .left .right -->
  <!-- 系统修饰键：.ctrl .alt .shift .meta -->
  <!-- 其他：.self .capture .passive .exact -->
</template>
```

## 3.6 表单绑定 v-model

`v-model` 在表单元素上是"值绑定 + 事件监听"的语法糖：

```vue
<template>
  <!-- 等价于 :value="msg" @input="msg = $event.target.value" -->
  <input v-model="msg" />

  <!-- textarea -->
  <textarea v-model="text"></textarea>

  <!-- checkbox：单个为布尔，多个为数组 -->
  <input type="checkbox" v-model="checked" />
  <input type="checkbox" value="A" v-model="checkList" />
  <input type="checkbox" value="B" v-model="checkList" />

  <!-- radio -->
  <input type="radio" value="男" v-model="gender" />
  <input type="radio" value="女" v-model="gender" />

  <!-- select -->
  <select v-model="selected">
    <option value="">请选择</option>
    <option value="a">A</option>
    <option value="b">B</option>
  </select>

  <!-- 修饰符 -->
  <input v-model.lazy="msg2" />    <!-- 默认 input 事件，.lazy 改为 change 事件 -->
  <input v-model.number="num" />   <!-- 自动转 number -->
  <input v-model.trim="text2" />   <!-- 去首尾空格 -->
</template>
```

**v-model 绑定组件**（见第五章 defineModel）。

## 3.7 计算属性 computed

计算属性用于声明式地派生数据，基于响应式依赖缓存，只有依赖变化时才重新计算：

```vue
<template>
  <p>原价：{{ price }}</p>
  <p>折后价：{{ discountedPrice }}</p>
  <p>商品列表数量：{{ itemsTotal }}</p>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const price = ref(100)
const discount = ref(0.8)

// 只读计算属性
const discountedPrice = computed(() => price.value * discount.value)

// 可写计算属性（很少用）
const firstName = ref('张')
const lastName = ref('三')
const fullName = computed({
  get: () => firstName.value + lastName.value,
  set: (val) => {
    firstName.value = val.slice(0, 1)
    lastName.value = val.slice(1)
  }
})

// 配合 v-for 做过滤/排序
const items = ref([1, 2, 3, 4, 5])
const itemsTotal = computed(() => items.value.reduce((s, n) => s + n, 0))
</script>
```

**computed 与 methods 的区别**：

- computed 会基于依赖缓存：依赖没变，多次访问走缓存，不再执行函数体。
- methods 每次调用都执行函数体。
- 有"根据已有数据派生"的需求优先用 computed，避免在模板里写复杂表达式。

## 3.8 侦听器 watch / watchEffect

`watch`：监听特定响应式源，副作用/异步操作场景使用。

```vue
<script setup lang="ts">
import { ref, watch, watchEffect } from 'vue'

const count = ref(0)
const name = ref('')
const user = reactive({ id: 1, age: 20 })

// 1. 监听单个 ref
watch(count, (newVal, oldVal) => {
  console.log('count 变化:', oldVal, '->', newVal)
})

// 2. 监听 getter（响应式对象属性）
watch(
  () => user.age,
  (newVal, oldVal) => {
    if (newVal > 18) console.log('成年')
  }
)

// 3. 监听多个源
watch([count, name], ([newCount, newName], [oldCount, oldName]) => {
  console.log('多个源变化')
})

// 4. 监听响应式对象整体（默认深层监听，oldVal 为浅拷贝）
watch(user, (newVal, oldVal) => {
  console.log('user 变化')
})

// 5. 选项：deep 深层 / immediate 立即执行一次 / flush 执行时机
watch(
  () => user,
  (val) => console.log('deep', val),
  { deep: true, immediate: true, flush: 'post' }  // flush: 'pre' | 'post' | 'sync'
)
</script>
```

`watchEffect`：自动追踪回调内访问的所有响应式依赖，依赖变化立即重新执行，无需指定来源。适合"立即执行 + 自动收集依赖"的场景（如记录日志、防抖请求）：

```vue
<script setup lang="ts">
import { ref, watchEffect } from 'vue'

const id = ref(1)
const data = ref(null)

watchEffect(async () => {
  // 访问 id，id 变化时自动重跑
  const res = await fetch(`/api/user/${id.value}`)
  data.value = await res.json()
})

// 停止侦听：返回的 stop 函数
const stop = watchEffect(() => {})
setTimeout(stop, 5000)   // 5 秒后停止

// 清理副作用：onCleanup（如取消请求、清除定时器）
watchEffect((onCleanup) => {
  const timer = setInterval(() => console.log('tick'), 1000)
  onCleanup(() => clearInterval(timer))
})
</script>
```

| API | 场景 |
| --- | --- |
| watch | 明确指定监听源，需要拿到新旧值，非立即执行 |
| watchEffect | 回调里用到的依赖都要跟踪，希望立即执行 |

# 四、响应式系统

> 响应式是 Vue 的基石：数据变化自动驱动视图更新。Vue3 基于 ES6 `Proxy` 重写。

## 4.1 响应式核心 API

```vue
<script setup lang="ts">
import { ref, reactive, readonly, shallowRef, shallowReactive, toRef, toRefs } from 'vue'

// ---------- ref：基础类型 / 也可包对象 ----------
const count = ref(0)
count.value++                     // 修改用 .value
const objRef = ref({ name: 'vue' })
objRef.value.name = 'vue3'        // 深层响应式

// ---------- reactive：对象 / 数组 ----------
const user = reactive({ name: '张三', age: 20 })
user.age = 21                     // 直接修改，无需 .value
const list = reactive([1, 2, 3])
list.push(4)

// 注意：reactive 的响应性会因"解构/传参"丢失（见 4.3）
const { age } = user              // age 变成普通数字，失去响应性！

// ---------- readonly：只读代理 ----------
const frozen = readonly({ count: 0 })
// frozen.count = 1   // 报错：无法修改

// ---------- shallowRef / shallowReactive：浅层响应 ----------
const shallow = shallowRef({ a: 1 })
shallow.value.a = 2               // 不触发更新（深层不响应）
shallow.value = { a: 3 }          // 触发更新（替换整个 value 才响应）
</script>
```

### ref 与 reactive 的选用

| 场景 | 推荐 | 原因 |
| --- | --- | --- |
| 基础类型（string/number/boolean） | ref | reactive 无法处理基础类型 |
| 对象、数组 | 两者皆可，Vue 官方建议默认用 ref | ref 的 `.value` 统一心智；reactive 解构易丢响应性 |
| 需要解构导出 | ref | 用 toRefs 转换后再解构 |
| 嵌套对象整体替换 | ref | `obj.value = {}` 直接替换 |

**推荐：默认统一使用 ref，需要深层解构时配合 toRefs。**

## 4.2 toRef 与 toRefs：解构保持响应性

`toRef` 将响应式对象的某个属性转换为独立 ref；`toRefs` 批量转换全部属性。常用于"解构后仍保持响应性"：

```vue
<script setup lang="ts">
import { reactive, toRef, toRefs } from 'vue'

const state = reactive({ name: '张三', age: 20 })

// toRefs：所有属性转 ref，解构后仍响应
const { name, age } = toRefs(state)
name.value = '李四'          // state.name 同步更新，模板也会更新

// toRef：单个属性转 ref
const ageRef = toRef(state, 'age')
ageRef.value = 25            // state.age 同步更新
</script>
```

注意：`toRefs` 转换的是"快照"，属性新增/删除不会自动包含，新增属性需再次转换。

## 4.3 响应性丢失的常见场景

```vue
<script setup lang="ts">
import { reactive } from 'vue'

const user = reactive({ name: '张三', age: 20 })

// 场景1：直接解构 -> age 变普通值
const { age } = user                 // 丢失

// 场景2：作为参数传入 -> 函数内部修改不触发
function change(u: { age: number }) {
  u.age = 30                          // 若外部依赖此响应，将不更新
}
change(user)

// 场景3：返回值给 const -> 丢失
const myUser = { ...user }            // 展开后是普通对象
</script>
```

**解决办法**：
1. 使用 `toRefs` / `toRef` 解构；
2. 函数参数用 getter 传入（`() => user.age`）；
3. 用 `computed` 派生；
4. 尽量不要把 reactive 对象的属性"拆出来单独存"。

## 4.4 响应式原理：Proxy 与依赖收集

Vue2 用 `Object.defineProperty` 劫持每个属性的 get/set，缺陷：

- 需要递归遍历对象，初始化开销大；
- 无法检测属性新增、删除；
- 数组索引、`length` 变化需要特殊 hack。

Vue3 用 `Proxy` 对整个对象做代理，天然支持新增/删除属性、数组方法（`push`/`splice` 等）。

工作原理（三个阶段）：

1. **依赖收集（track）**：渲染函数或 computed 读取响应式属性时，触发 getter，把当前"副作用"（effect）登记到该属性的依赖集合（`dep`）中。
2. **触发更新（trigger）**：属性被修改时，触发 setter，通知所有依赖该属性的 effect 重新执行。
3. **调度执行**：effect 触发更新时，Vue 会批量异步调度（下一个微任务 tick），合并同一轮多次修改，避免重复渲染。

简化的最小实现：

```js
// 伪代码：一个极简的响应式系统
const deps = new Map()               // target -> key -> Set(effects)
let activeEffect = null

function track(target, key) {
  if (!activeEffect) return
  // ... 把 activeEffect 加入 deps 对应集合
}

function trigger(target, key) {
  // ... 取出 deps 集合里的 effect 并逐个执行
}

function reactive(obj) {
  return new Proxy(obj, {
    get(target, key, receiver) {
      track(target, key)
      return Reflect.get(target, key, receiver)
    },
    set(target, key, value, receiver) {
      const res = Reflect.set(target, key, value, receiver)
      trigger(target, key)
      return res
    }
  })
}

const state = reactive({ count: 0 })
activeEffect = () => console.log('渲染', state.count)
track(state, 'count')
state.count = 1   // 输出 "渲染 1"
```

**实际编译优化**：模板会被编译成 Block Tree，静态节点会被提升到渲染函数外部只创建一次，动态节点会标记 `patchFlag`（如 `TEXT`、`CLASS`、`PROPS`），diff 时只比对动态部分，大幅提升性能。

## 4.5 其他响应式工具

- `computed`：见 3.7。
- `triggerRef(shallowRef)`：手动强制触发浅层 ref 更新。
- `isRef` / `isReactive` / `isProxy`：类型判断。
- `unref`：`val = isRef(val) ? val.value : val`。
- `toRaw`：获取被代理的原始对象（用于临时读值绕过代理）。
- `markRaw`：标记对象永不被转换为响应式（如存放第三方库实例）。

# 五、组件通信

> 组件是 Vue 应用的基本单元。组件间通信是高频面试点，也是日常开发核心。

## 5.1 组件通信方式总览

| 方式 | 方向 | 适用场景 |
| --- | --- | --- |
| `props` | 父 -> 子 | 父组件传递数据给子组件 |
| `emit`（事件） | 子 -> 父 | 子组件通知父组件并传值 |
| `defineExpose` | 子 -> 父 | 父通过 ref 直接调用子组件内部方法 |
| `defineModel` | 双向 | `v-model` 双向绑定语法糖 |
| `provide / inject` | 祖先 -> 后代 | 跨层级传递，无需逐层传 props |
| Slot 插槽 | 父 -> 子 | 父组件向子组件传入模板内容 |
| 全局状态（Pinia） | 任意 | 复杂业务共享状态 |
| `mitt`（事件总线） | 任意 | 替代 Vue2 `$bus` 的轻量方案 |
| 路由参数 | 任意 | 页面间传参 |

## 5.2 父传子：defineProps

"父传子"通信工具：子组件声明接收的 props，父组件通过属性传递。

```vue
<!-- 父组件 Parent.vue -->
<template>
  <Child :msg="message" :user="user" :count="n" @update="onUpdate" />
</template>
<script setup lang="ts">
import { ref } from 'vue'
import Child from './Child.vue'

const message = ref('来自父组件的数据')
const user = ref({ name: '张三' })
const n = ref(10)
const onUpdate = (val: string) => console.log('子组件通知:', val)
</script>
```

```vue
<!-- 子组件 Child.vue -->
<template>
  <div>{{ msg }} - {{ user.name }} - {{ count }}</div>
</template>
<script setup lang="ts">
// 方式一：字符串数组（简单）
const props = defineProps(['msg', 'user', 'count'])

// 方式二：对象声明（可带类型校验 + 默认值 + 必填）
const props2 = defineProps({
  msg: {
    type: String,
    default: '默认消息',
    required: true
  },
  user: {
    type: Object,
    default: () => ({ name: '默认' })   // 对象/数组必须用工厂函数
  },
  count: {
    type: Number,
    default: 0,
    validator: (val: number) => val >= 0   // 自定义校验
  },
  // 类型可以是 String/Number/Boolean/Array/Object/Date/Function/Symbol
  status: {
    type: [String, Number],   // 联合类型
    default: 'active'
  }
})
console.log(props.msg)   // 直接访问
</script>
```

**TS 推荐写法：泛型 + 命名类型**：

```vue
<script setup lang="ts">
interface UserInfo {
  id: number
  name: string
}
const props = defineProps<{
  msg: string
  user: UserInfo
  count?: number          // 可选
  withDefault?: boolean   // 配合 withDefaults 给默认值
}>()

// 默认值：withDefaults
withDefaults(defineProps<{ page: number; title?: string }>(), {
  page: 1,
  title: '默认标题'
})
</script>
```

注意：`props` 是只读的，子组件不允许直接修改 props（会告警）。如需修改，用局部 ref 初始化或 emit 通知父组件。

## 5.3 子传父：defineEmits

"子传父"通信工具：子组件声明可触发的事件，通过 `emit` 函数向父组件发送数据/通知。

```vue
<!-- 子组件 Child.vue -->
<template>
  <button @click="handleClick">触发事件</button>
  <input @input="emit('update', $event.target.value)" />
</template>
<script setup lang="ts">
// 声明事件（数组或对象形式），返回 emit 函数
const emit = defineEmits(['my-event', 'update', 'submit'])

// 对象形式可以校验 emit 参数
const emit2 = defineEmits<{
  (e: 'submit', payload: { ok: boolean }): void
}>()

const handleClick = () => {
  emit('my-event', 'Hello Parent!')
}
</script>
```

```vue
<!-- 父组件 Parent.vue -->
<template>
  <Child
    @my-event="handleMyEvent"
    @submit="(payload) => console.log('submit', payload)"
  />
</template>
<script setup lang="ts">
const handleMyEvent = (msg: string) => {
  console.log(msg)   // 'Hello Parent!'
}
</script>
```

## 5.4 父访问子：defineExpose

子组件通过 `defineExpose` 暴露内部成员，父组件通过 `ref` 拿到组件实例后直接调用：

```vue
<!-- 子组件 ScrollPane.vue -->
<template>
  <div class="scroll-pane">
    <slot />
  </div>
</template>
<script setup lang="ts">
const scrollToTop = () => {
  console.log('滚到顶部')
}
const scrollToStart = () => {
  console.log('滚到开头')
}

// 不写 defineExpose 时，<script setup> 内部默认全部对外封闭
defineExpose({ scrollToTop, scrollToStart })
</script>
```

```vue
<!-- 父组件 Parent.vue -->
<template>
  <ScrollPane ref="scrollPaneRef" />
  <button @click="callChild">调用子组件方法</button>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import ScrollPane from './ScrollPane.vue'

const scrollPaneRef = ref<InstanceType<typeof ScrollPane> | null>(null)

const callChild = () => {
  scrollPaneRef.value?.scrollToStart()   // 直接调用子组件暴露的方法
  scrollPaneRef.value?.scrollToTop()
}
</script>
```

## 5.5 双向绑定：defineModel（v-model 组件化）

Vue3.4+ 提供 `defineModel`，简化组件上 `v-model` 的双向绑定实现。

```vue
<!-- 子组件 MyInput.vue -->
<template>
  <input :value="model" @input="onInput" />
</template>
<script setup lang="ts">
// modelValue 自动与父组件的 v-model 双向绑定
const model = defineModel<string>({ default: '' })

// 多个 v-model 绑定（Vue3.3+ 支持）
const modelTitle = defineModel<string>('title', { required: true })

const onInput = (e: Event) => {
  model.value = (e.target as HTMLInputElement).value   // 修改 model 即更新父组件
}
</script>
```

```vue
<!-- 父组件 Parent.vue -->
<template>
  <MyInput v-model="text" v-model:title="title" />
</template>
<script setup lang="ts">
import { ref } from 'vue'
import MyInput from './MyInput.vue'

const text = ref('')
const title = ref('默认标题')
</script>
```

## 5.6 跨层级传值：provide / inject

祖先组件 `provide` 提供数据，任意后代组件 `inject` 注入，跨多级传递而无需逐层 props：

```vue
<!-- 祖先组件 App.vue -->
<template>
  <ChildComp />
</template>
<script setup lang="ts">
import { provide, ref, readonly } from 'vue'
import ChildComp from './ChildComp.vue'

const theme = ref('light')
const user = { name: '张三' }

// 顶层提供（默认不是响应式的，除非传 ref / reactive）
provide('theme', theme)
provide('user', readonly(user))   // readonly 防止后代误改

// 也可以提供函数，让后代修改祖先状态
provide('changeTheme', (val: string) => (theme.value = val))
</script>
```

```vue
<!-- 后代组件 GrandChild.vue -->
<template>
  <div>{{ theme }} - {{ user.name }}</div>
</template>
<script setup lang="ts">
import { inject } from 'vue'

// 注入祖先提供的数据
const theme = inject('theme', 'light')          // 第二个参数为默认值
const user = inject('user', { name: '未知' })
const changeTheme = inject<((v: string) => void) | undefined>('changeTheme')

const handleClick = () => changeTheme?.('dark')
</script>
```

**TS 安全推荐**：用 `InjectionKey` 类型化注入：

```ts
// keys.ts
import type { InjectionKey, Ref } from 'vue'
export const themeKey: InjectionKey<Ref<string>> = Symbol('theme')
export const userKey: InjectionKey<{ name: string }> = Symbol('user')
```

**注意**：provide/inject 是非响应式绑定（除非传 ref/reactive），且无法被后代直接改值（推荐 provide 函数或 readonly）。

## 5.7 插槽 Slot

插槽让父组件向子组件传递"模板内容"。

### 默认插槽 / 具名插槽 / 作用域插槽

```vue
<!-- 子组件 Card.vue -->
<template>
  <div class="card">
    <!-- 默认插槽 -->
    <div class="card-body">
      <slot>默认内容</slot>
    </div>
    <!-- 具名插槽 -->
    <div class="card-footer">
      <slot name="footer">默认 footer</slot>
    </div>
    <!-- 作用域插槽：向父组件暴露子组件数据 -->
    <div class="card-list">
      <slot name="item" :item="currentItem" :index="currentIndex">
        {{ currentItem }}
      </slot>
    </div>
  </div>
</template>
<script setup lang="ts">
const items = ['a', 'b', 'c']
const currentItem = items[0]
const currentIndex = 0
</script>
```

```vue
<!-- 父组件使用 -->
<template>
  <Card>
    <!-- 默认插槽 -->
    <p>这是主体内容</p>

    <!-- 具名插槽（v-slot:footer 简写 #footer） -->
    <template #footer>
      <button>底部按钮</button>
    </template>

    <!-- 作用域插槽：解构子组件暴露的 slot props -->
    <template #item="{ item, index }">
      <span>{{ index }}: {{ item }}</span>
    </template>
  </Card>
</template>
```

### 动态插槽名

```vue
<template>
  <Comp>
    <template #[dynamicSlotName]>动态插槽内容</template>
  </Comp>
</template>
<script setup lang="ts">
const dynamicSlotName = 'header'
</script>
```

## 5.8 动态组件与 KeepAlive

```vue
<template>
  <!-- 动态组件：is 决定渲染哪个组件 -->
  <component :is="currentComp" :msg="msg" />

  <!-- KeepAlive：切换时缓存组件状态，避免重新渲染 -->
  <KeepAlive :include="['CompA', 'CompB']" :max="10">
    <component :is="currentComp" />
  </KeepAlive>
</template>

<script setup lang="ts">
import { ref, shallowRef, markRaw } from 'vue'
import CompA from './CompA.vue'
import CompB from './CompB.vue'

// 注意：用 shallowRef + markRaw 避免把组件对象变成响应式
const currentComp = shallowRef(markRaw(CompA))
const msg = ref('hello')
</script>
```

KeepAlive 的生命周期钩子：被缓存的组件会多出 `onActivated`（激活）与 `onDeactivated`（失活）。

# 六、组合式 API 与生命周期

## 6.1 <script setup> 语法

`<script setup>` 是组合式 API 的编译时语法糖，写法更简洁：

- 顶层变量、函数自动暴露给模板；
- `defineProps` / `defineEmits` / `defineExpose` / `defineModel` 是编译器宏，无需 import；
- 组件、组合式函数直接 import 使用；
- 支持 `await` 顶层 await（组件会自动变成异步组件）。

```vue
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Child from './Child.vue'

const count = ref(0)
const add = () => count.value++

// 组件自动注册，模板直接用 <Child />
</script>
```

## 6.2 setup 钩子与生命周期

组合式 API 生命周期钩子（在 `<script setup>` 中直接调用）：

| 选项式 API | 组合式 API | 触发时机 |
| --- | --- | --- |
| beforeCreate | 无需（setup 中就是） | 实例初始化前 |
| created | 无需（setup 中就是） | 实例创建后 |
| beforeMount | onBeforeMount | 挂载前 |
| mounted | onMounted | 挂载后（DOM 就绪，适合请求/操作 DOM） |
| beforeUpdate | onBeforeUpdate | 数据变化、重新渲染前 |
| updated | onUpdated | 重新渲染后 |
| beforeUnmount | onBeforeUnmount | 卸载前（清理定时器、取消订阅） |
| unmounted | onUnmounted | 卸载后 |
| errorCaptured | onErrorCaptured | 捕获子孙组件错误 |
| activated | onActivated | 被 KeepAlive 缓存组件激活 |
| deactivated | onDeactivated | 被 KeepAlive 缓存组件失活 |

```vue
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

const count = ref(0)

onMounted(() => {
  console.log('组件挂载完成')
})

onBeforeUnmount(() => {
  console.log('组件卸载前，清理资源')
})
</script>
```

生命周期时序示意：

```
setup (created) → onBeforeMount → onMounted → onBeforeUpdate → updated → ... → onBeforeUnmount → onUnmounted
```

## 6.3 组合式函数（Composables）

把"有状态的逻辑"抽成以 `use` 开头的函数，在组件间复用。它内部可以使用 ref、computed、生命周期钩子，天然具备响应式。

示例：useMouse 鼠标位置监听

```ts
// src/composables/useMouse.ts
import { ref, onMounted, onUnmounted } from 'vue'

export function useMouse() {
  const x = ref(0)
  const y = ref(0)

  function update(event: MouseEvent) {
    x.value = event.pageX
    y.value = event.pageY
  }

  onMounted(() => window.addEventListener('mousemove', update))
  onUnmounted(() => window.removeEventListener('mousemove', update))

  return { x, y }
}
```

```vue
<!-- 组件中使用 -->
<script setup lang="ts">
import { useMouse } from '@/composables/useMouse'
const { x, y } = useMouse()   // 解构使用，x/y 仍是响应式 ref
</script>
```

组合式函数命名约定与最佳实践：

- 命名以 `use` 开头；
- 尽量返回 ref 而非 reactive（便于解构）；
- 单一职责，一个函数只做一件事；
- 可通过参数接收外部状态，实现函数间的组合；
- 内部使用生命周期钩子是安全的（会被绑定到调用它的组件）。

示例：组合两个 composables 实现防抖搜索

```ts
// src/composables/useDebounce.ts
import { ref, watch, type Ref } from 'vue'

export function useDebounce<T>(source: Ref<T>, delay = 300) {
  const debounced = ref(source.value) as Ref<T>
  let timer: ReturnType<typeof setTimeout> | null = null

  watch(source, (val) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      debounced.value = val
    }, delay)
  })

  return debounced
}
```

## 6.4 选项式 API 快速回顾（对比）

```vue
<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  data() {
    return { count: 0 }
  },
  computed: {
    double() {
      return this.count * 2
    }
  },
  watch: {
    count(newVal, oldVal) {
      console.log(newVal, oldVal)
    }
  },
  methods: {
    add() {
      this.count++
    }
  },
  mounted() {
    console.log('mounted')
  }
})
</script>
```

两种风格都能写，新项目推荐组合式；团队已有 Vue2 项目可先用选项式平滑迁移。

# 七、工程化：TypeScript + Vite

## 7.1 Vite 与 TypeScript 基础配置

```ts
// vite.config.ts
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))   // 路径别名
    }
  },
  server: {
    port: 5173,
    open: true,
    proxy: {
      // 开发环境代理，解决跨域
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],        // 手动分包
          'element-plus': ['element-plus']
        }
      }
    }
  }
})
```

环境变量：

```
# .env.development
VITE_APP_TITLE=dev环境
VITE_API_BASE=/api

# .env.production
VITE_APP_TITLE=prod环境
VITE_API_BASE=https://api.example.com
```

```ts
// 使用环境变量（必须以 VITE_ 前缀才会暴露给客户端）
const apiBase = import.meta.env.VITE_API_BASE
console.log(import.meta.env.MODE)      // development / production
```

## 7.2 TypeScript 类型推导

`<script setup lang="ts">` 中，props/emit 有完整的类型推导：

```vue
<script setup lang="ts">
interface Props {
  title: string
  count: number
  tags?: string[]
}

// 泛型声明 props，类型安全
const props = defineProps<Props>()

// withDefaults 给默认值
withDefaults(defineProps<Props>(), {
  tags: () => []
})

// emit 类型化
const emit = defineEmits<{
  (e: 'change', value: string): void
  (e: 'submit', payload: { ok: boolean }): void
}>()
</script>
```

## 7.3 组合式函数 + TS 泛型

```ts
export function useAsyncData<T>(fetcher: () => Promise<T>) {
  const data = ref<T | null>(null)
  const loading = ref(false)
  const error = ref<Error | null>(null)

  async function run() {
    loading.value = true
    error.value = null
    try {
      data.value = await fetcher()
    } catch (e) {
      error.value = e as Error
    } finally {
      loading.value = false
    }
  }

  return { data, loading, error, run }
}
```

# 八、路由 Vue Router 4

## 8.1 安装与基本配置

```
npm install vue-router@4
```

```ts
// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Home from '@/views/Home.vue'

// 静态导入 + 懒加载（推荐页面用懒加载）
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('@/views/About.vue'),   // 懒加载，按需分包
    meta: { title: '关于我们', requiresAuth: true }  // 路由元信息
  },
  {
    path: '/user/:id',                              // 动态路由参数
    name: 'user',
    component: () => import('@/views/User.vue'),
    props: true                                      // 把 params 作为 props 传入组件
  },
  {
    path: '/redirect/:path(.*)',                    // 正则捕获
    component: () => import('@/views/Redirect.vue')
  },
  {
    path: '/:pathMatch(.*)*',                        // 404 兜底
    name: 'not-found',
    component: () => import('@/views/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),   // HTML5 history 模式；createWebHashHistory() 为 hash 模式
  routes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition ?? { top: 0 }   // 切换路由回到顶部
  }
})

export default router
```

在入口挂载：

```ts
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

createApp(App).use(router).mount('#app')
```

## 8.2 模板中使用

```vue
<template>
  <!-- 声明式导航 -->
  <router-link to="/about">关于</router-link>
  <router-link :to="{ name: 'user', params: { id: 123 } }">用户详情</router-link>

  <!-- 路由出口：组件渲染的位置 -->
  <router-view />
</template>
```

## 8.3 编程式导航

```vue
<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 跳转
const goAbout = () => router.push('/about')
const goUser = () => router.push({ name: 'user', params: { id: route.params.id } })
const goQuery = () => router.push({ path: '/search', query: { q: 'vue' } })
const goReplace = () => router.replace('/about')   // 不产生历史记录
const goBack = () => router.back()
const goForward = () => router.forward()

// 当前路由信息
console.log(route.path)          // /user/123
console.log(route.params)        // { id: '123' }
console.log(route.query)         // { q: 'vue' }
console.log(route.meta)          // 元信息
console.log(route.name)          // 'user'
</script>
```

注意：`useRoute()` 返回的 `route.params` / `route.query` 变化时，组件默认会复用，需要监听：

```vue
<script setup lang="ts">
import { watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 参数变化时重新请求
watch(() => route.params.id, (newId) => {
  fetchUser(newId as string)
})
</script>
```

## 8.4 嵌套路由

```ts
const routes = [
  {
    path: '/user',
    component: () => import('@/views/user/Layout.vue'),
    children: [
      { path: '', name: 'user-home', component: () => import('@/views/user/Home.vue') },
      { path: 'profile', name: 'user-profile', component: () => import('@/views/user/Profile.vue') },
      { path: 'settings', component: () => import('@/views/user/Settings.vue') }
    ]
  }
]
```

父组件 Layout 里必须放 `<router-view />` 用于渲染子路由：

```vue
<template>
  <div class="layout">
    <SideMenu />
    <router-view />   <!-- 子路由渲染位置 -->
  </div>
</template>
```

## 8.5 导航守卫

**全局前置守卫**（最常用，用于登录校验、设置标题）：

```ts
router.beforeEach(async (to, from) => {
  const token = localStorage.getItem('token')

  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 我的站点` : '我的站点'

  // 需要登录但未登录 -> 跳转登录页
  if (to.meta.requiresAuth && !token) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  // 已登录访问登录页 -> 跳回首页
  if (to.name === 'login' && token) {
    return { path: '/' }
  }

  return true
})
```

**全局解析守卫 / 后置守卫**：

```ts
router.beforeResolve(async (to) => { /* 路由跳转前最后一步，可在此确认数据 */ })
router.afterEach((to, from) => { /* 跳转完成后，埋点统计 */ })
```

**路由独享守卫**：

```ts
const routes = [
  {
    path: '/admin',
    component: () => import('@/views/Admin.vue'),
    beforeEnter: (to, from) => {
      // 只有管理员能进
      return localStorage.getItem('role') === 'admin' || '/forbidden'
    }
  }
]
```

**组件内守卫**：

```vue
<script setup lang="ts">
import { onBeforeRouteLeave, onBeforeRouteUpdate } from 'vue-router'

onBeforeRouteLeave(() => {
  // 离开前校验（如表单未保存提醒）
})

onBeforeRouteUpdate((to, from) => {
  // 同组件内参数变化时触发
})
</script>
```

守卫执行顺序（一次完整导航）：

```
beforeRouteLeave → router.beforeEach → beforeEnter → 组件 beforeRouteUpdate → beforeResolve → 导航确认 → DOM 更新 → afterEach → 触发 beforeRouteEnter 的 next 回调
```

## 8.6 路由懒加载与代码分包

```ts
const routes = [
  // 全部用 () => import() 按需加载
  { path: '/', component: () => import('@/views/Home.vue') },
  { path: '/detail', component: () => import('@/views/Detail.vue') },
  // 或者配置 webpackChunkName 控制分包名称（Vite 也支持注释）
  { path: '/list', component: () => import(/* webpackChunkName: "list" */ '@/views/List.vue') }
]
```

# 九、状态管理 Pinia

## 9.1 为什么用 Pinia

- 官方推荐，Vue3 的默认状态管理方案（Vuex 5 即 Pinia）；
- 支持组合式 API 风格，TypeScript 类型推导友好；
- 无 mutation，直接修改 state，API 简洁；
- 支持 devtools、模块化、持久化插件。

## 9.2 安装与定义 store

```
npm install pinia
```

```ts
// src/main.ts
import { createPinia } from 'pinia'
const pinia = createPinia()
app.use(pinia)
```

**方式一：选项式风格（类似 Vuex）**

```ts
// src/stores/user.ts
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    name: '张三',
    roles: [] as string[]
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    displayName: (state) => state.name || '未登录'
  },
  actions: {
    login(token: string) {
      this.token = token
      localStorage.setItem('token', token)
    },
    async fetchRoles() {
      // 支持异步 action
      const res = await fetch('/api/roles')
      this.roles = await res.json()
    }
  }
})
```

**方式二：组合式风格（推荐）**

```ts
// src/stores/counter.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)

  function increment(step = 1) {
    count.value += step
  }
  function reset() {
    count.value = 0
  }

  return { count, doubleCount, increment, reset }
})
```

## 9.3 在组件中使用

```vue
<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useCounterStore } from '@/stores/counter'
import { useUserStore } from '@/stores/user'

// store 实例（响应式对象）
const counter = useCounterStore()
const user = useUserStore()

// 直接修改 state
counter.count++

// 调用 action
counter.increment(5)
user.login('abc123')

// 注意：解构 store 会丢失响应性，要用 storeToRefs
const { count, doubleCount } = storeToRefs(counter)   // ref，可解构
const { login, fetchRoles } = user                    // 方法直接解构没问题
</script>
```

**重要**：直接 `const { count } = counter` 解构出来的不是响应式的。必须用 `storeToRefs()` 转换 state/getters，actions 方法可以直接解构。

## 9.4 持久化

使用插件 `pinia-plugin-persistedstate`：

```
npm install pinia-plugin-persistedstate
```

```ts
// main.ts
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
```

```ts
export const useUserStore = defineStore('user', {
  state: () => ({ token: '', name: '' }),
  persist: {
    key: 'user-store',            // 存储 key
    storage: localStorage,        // 存储介质，默认 localStorage
    pick: ['token']               // 只持久化部分字段
  }
})
```

# 十、进阶特性

## 10.1 自定义指令

用于复用"操作 DOM"的逻辑。Vue3 中生命周期钩子与组件一致：

```ts
// src/directives/vFocus.ts
import type { Directive, DirectiveBinding } from 'vue'

// 全局自定义指令
export const vFocus: Directive<HTMLElement, string> = {
  mounted(el: HTMLElement) {
    el.focus()
  }
}

// 完整指令钩子示例（防重复点击）
export const vClickOnce: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const original = el.onclick
    el.onclick = (event) => {
      el.style.pointerEvents = 'none'
      binding.value?.()
    }
  }
}
```

```ts
// main.ts 全局注册
import { vFocus, vClickOnce } from '@/directives'
app.directive('focus', vFocus)
app.directive('click-once', vClickOnce)
```

```vue
<!-- 使用：v-focus、v-click-once -->
<template>
  <input v-focus />
  <button v-click-once="handleClick">只可点一次</button>
</template>
```

指令钩子：`created` / `beforeMount` / `mounted` / `beforeUpdate` / `updated` / `beforeUnmount` / `unmounted`。

## 10.2 Teleport 传送门

将子组件渲染到 DOM 的任意位置（如 body），常用于弹窗、抽屉、提示：

```vue
<!-- Modal.vue -->
<template>
  <!-- 传送到 body 下，避免被父级 overflow/transform 影响 -->
  <Teleport to="body">
    <div v-if="visible" class="modal-mask">
      <div class="modal">
        <slot />
        <button @click="emit('close')">关闭</button>
      </div>
    </div>
  </Teleport>
</template>
<script setup lang="ts">
defineProps<{ visible: boolean }>()
const emit = defineEmits<{ (e: 'close'): void }>()
</script>
```

```vue
<!-- 禁用传送（如 SSR 条件） -->
<Teleport :disabled="isServer">
```

## 10.3 Suspense 与异步组件

`defineAsyncComponent` 定义异步组件；`<Suspense>` 包裹异步组件，提供加载/回退状态：

```ts
import { defineAsyncComponent } from 'vue'

// 常规异步组件
const AsyncComp = defineAsyncComponent(() => import('@/components/Heavy.vue'))

// 带加载/错误状态
const AsyncCompWithLoading = defineAsyncComponent({
  loader: () => import('@/components/Heavy.vue'),
  loadingComponent: () => import('@/components/Loading.vue'),
  errorComponent: () => import('@/components/ErrorComp.vue'),
  delay: 200,               // 延迟显示 loading
  timeout: 3000             // 超时报错
})
```

```vue
<template>
  <Suspense>
    <!-- 异步内容 -->
    <AsyncComp />

    <!-- fallback：加载完成前显示 -->
    <template #fallback>
      <Loading />
    </template>
  </Suspense>
</template>
```

配合 `<script setup>` 顶层 await：

```vue
<script setup lang="ts">
const res = await fetch('/api/data')          // 顶层 await
const data = await res.json()
</script>
```

## 10.4 插槽外传 / 渲染函数与 JSX

Vue3 也支持 JSX / TSX（`@vitejs/plugin-vue` + `@vitejs/plugin-vue-jsx`）：

```tsx
// Component.tsx
import { defineComponent, ref } from 'vue'

export default defineComponent({
  setup() {
    const count = ref(0)
    return () => (
      <button onClick={() => count.value++}>Count: {count.value}</button>
    )
  }
})
```

## 10.5 插件机制

插件用于为应用添加全局功能，暴露 `install(app)`：

```ts
// src/plugins/myPlugin.ts
import type { App } from 'vue'

export default {
  install(app: App, options?: Record<string, unknown>) {
    // 注册全局组件
    app.component('GlobalBtn', ...)
    // 注册全局指令
    app.directive('focus', ...)
    // 提供全局属性
    app.config.globalProperties.$t = (key: string) => `translate:${key}`
    // 全局混入
    app.mixin({ created() { /* ... */ } })
  }
}
```

```ts
// 使用
import myPlugin from '@/plugins/myPlugin'
app.use(myPlugin, { defaultLang: 'zh' })
```

# 十一、实战与最佳实践

## 11.1 Axios 封装

```ts
// src/api/request.ts
import axios from 'axios'
import type { AxiosRequestConfig } from 'axios'

// 1. 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  timeout: 10000
})

// 2. 请求拦截器：携带 token
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

// 3. 响应拦截器：统一解包、统一错误提示
service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      // 业务错误
      console.error(res.msg)
      return Promise.reject(new Error(res.msg))
    }
    return res.data          // 直接返回业务数据
  },
  (error) => {
    if (error.response?.status === 401) {
      // 未登录，跳登录页
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 4. 泛型封装
export function request<T = unknown>(config: AxiosRequestConfig): Promise<T> {
  return service.request(config) as Promise<T>
}
```

```ts
// src/api/user.ts
import { request } from './request'

export interface UserInfo {
  id: number
  name: string
}

export function getUser(id: number) {
  return request<UserInfo>({ url: `/user/${id}`, method: 'get' })
}
```

## 11.2 Element Plus 集成

```
npm install element-plus
```

按需自动导入（推荐）：

```
npm install -D unplugin-vue-components unplugin-auto-import
```

```ts
// vite.config.ts
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({ resolvers: [ElementPlusResolver()] }),
    Components({ resolvers: [ElementPlusResolver()] })
  ]
})
```

```vue
<template>
  <el-button type="primary" @click="visible = true">打开对话框</el-button>
  <el-dialog v-model="visible" title="提示" width="30%">
    <span>这是 Element Plus 对话框</span>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const visible = ref(false)
</script>
```

## 11.3 常用组合式函数清单

| 组合式函数 | 说明 |
| --- | --- |
| `useMouse()` | 鼠标坐标 |
| `useFetch(url)` | 封装请求，含 loading/error/重试 |
| `useDebounceRef(src, delay)` | 防抖值 |
| `useThrottle` | 节流 |
| `useStorage(key, init)` | localStorage 响应式封装 |
| `useEventListener(el, event, handler)` | 自动绑定/解绑事件 |
| `useIntersectionObserver` | 懒加载、进入视口检测 |

## 11.4 性能优化实践

1. **路由懒加载**：页面组件全部用 `() => import()`。
2. **异步组件**：大组件用 `defineAsyncComponent`。
3. **v-memo**（Vue3.2+）：对静态列表做记忆化，跳过不必要的更新：
   ```vue
   <div v-for="item in list" v-memo="[item.id, item.updatedAt]">
     {{ item.title }}
   </div>
   ```
4. **函数式组件与静态提升**：静态节点自动提升，无需手动优化。
5. **按需引入 UI 库**：Element Plus 等使用 unplugin 按需加载，减小包体积。
6. **代码分包**：`build.rollupOptions.output.manualChunks` 拆分 vendor。
7. **图片懒加载**：`v-lazy` 自定义指令或三方库。
8. **避免过度响应式**：非响应式数据用 `markRaw` / `shallowRef`。
9. **使用 computed 缓存**：避免模板中重复调用方法。
10. **虚拟列表**：超长列表用 `v-virtual-scroller` 或 Element Plus 的 `el-virtual-scroll-list`。

## 11.5 常见坑与 FAQ

| 问题 | 原因与解决 |
| --- | --- |
| 解构 reactive 丢失响应性 | 用 `toRefs` / `storeToRefs` / computed |
| `ref` 在模板中忘记 `.value` | 模板中自动解包，`<script>` 中必须 `.value` |
| 数组 index 作为 key | 增删/排序时渲染错乱，用唯一 id |
| `v-if` 与 `v-for` 同元素 | Vue3 中 `v-if` 优先级更高，分开写 |
| `provide/inject` 不更新 | 传 ref/reactive 或函数 |
| `defineExpose` 没生效 | 必须在 `<script setup>` 中显式调用 |
| `reactive` 里存组件/类实例 | 用 `markRaw` 避免代理 |
| `watch` 监听 reactive 对象属性不触发 | 用 getter：`() => obj.prop` |
| 路由参数变化页面不刷新 | 监听 `route.params` 重新请求 |
| `<script setup>` 中组件名大写 | 组件命名规范：多单词 + PascalCase |
| 组件间数据"魔法耦合" | 优先 props/emit 或 Pinia，慎用事件总线 |

## 11.6 目录结构建议（中大型项目）

```
src/
├── api/                 # 接口层（按模块拆分）
├── assets/              # 静态资源
├── components/          # 通用组件（按需按模块分子目录）
├── composables/         # 组合式函数
├── directives/          # 自定义指令
├── layouts/             # 布局组件
├── plugins/             # 插件
├── router/              # 路由
├── stores/              # Pinia store
├── styles/              # 全局样式（variables/mixins）
├── utils/               # 工具函数
├── views/               # 页面
├── App.vue
└── main.ts
```

# 十二、附录

## 12.1 官方资源

- Vue3 官方文档：https://vuejs.org/ （中文：https://cn.vuejs.org/ ）
- Vue Router 4：https://router.vuejs.org/zh/
- Pinia：https://pinia.vuejs.org/zh/
- Vite：https://cn.vitejs.dev/
- Element Plus：https://element-plus.org/zh-CN/
- Naive UI：https://www.naiveui.com/
- VueUse（官方组合式工具库）：https://vueuse.org/

## 12.2 术语速查

| 术语 | 含义 |
| --- | --- |
| SFC | 单文件组件（.vue） |
| CSP | 组合式 API（Composition API） |
| OSP | 选项式 API（Options API） |
| vnode | 虚拟节点 |
| patchFlag | 编译时标记的动态更新类型 |
| effect | 副作用（渲染/计算属性/watch 的底层单位） |
| dep | 依赖集合 |
| Teleport | 传送门，渲染到指定 DOM 位置 |
| Composables | 组合式函数 |
| defineModel | v-model 的宏，简化双向绑定 |
| 响应式代理 | Proxy 包装后的响应式对象 |
