<h2 style="color:#4db6ac !important" >React 教程</h2>
> 【原创】2026-09-06

[TOCM]

[TOC]


# 一、React 简介

## 1.1 什么是 React

React 是一个用于构建用户界面的 JavaScript 库（注意是"库"而非"框架"）。它由 Meta（原 Facebook）开发并开源，于 2013 年发布。

它的核心思想可以用一个公式概括：

```
UI = f(state)
```

即：界面是状态（state）的纯函数。开发者只需要描述"在某种状态下界面长什么样"，当状态变化时，React 负责把界面更新到最新状态。

React 的特点：

- **组件化**：界面由组件（Component）组合而成，组件可复用、可嵌套、可独立维护。
- **声明式**：你描述"想要什么"，React 处理"怎么更新 DOM"。
- **单向数据流**：数据从父组件流向子组件，状态变化流向清晰、易于追踪与调试。
- **只做 View 层**：路由（React Router）、状态管理（Redux/Zustand）、请求等都需要搭配生态完成，是典型的"库 + 生态"方案。

React 本身不强制"全家桶"，你可以只引入它渲染一个小组件，也可以基于它构建大型 SPA，还可以使用 Next.js 等框架实现全栈应用。

## 1.2 函数组件 + Hooks 与类组件

React 16.8（2019 年）引入 Hooks 是一个分水岭。此前组件主要以"类组件（Class Component）"编写，之后官方全面推荐"函数组件（Function Component）+ Hooks"。

| 维度 | 类组件 | 函数组件 + Hooks（推荐） |
| --- | --- | --- |
| 定义方式 | `class X extends Component` | `function X(props) {}` |
| 状态 | `this.state` + `this.setState` | `useState` / `useReducer` |
| 生命周期 | `componentDidMount` 等固定钩子 | `useEffect` 统一表达副作用 |
| this 指向 | 需要 bind / 箭头函数 | 无 this，天然无绑定问题 |
| 逻辑复用 | HOC / Render Props（嵌套地狱） | 自定义 Hook `useXxx()` |
| 代码组织 | 同一业务逻辑分散到各生命周期 | 按逻辑关注点集中，可抽 Hook |
| 类型支持 | 较弱 | 与 TypeScript 结合极佳 |
| 心智负担 | 大（生命周期多、this 混乱） | 小（纯函数 + 状态） |

**结论**：新项目统一使用"函数组件 + Hooks"；老项目保留类组件，可在新代码中逐步迁移。二者可以共存。

## 1.3 React 生态版本对照

| 生态 | 说明 / 版本 |
| --- | --- |
| 核心框架 | React 19.x（2024-12 发布），18.x 仍是广泛使用的稳定基线 |
| 路由 | React Router v6（`react-router-dom`）；v7 起包名统一为 `react-router` |
| 状态管理 | Redux Toolkit / Zustand / Jotai / MobX |
| 全栈框架 | Next.js（App Router）/ Remix |
| 脚手架 | Vite（`create-react-app` 已停止维护，不再用于新项目） |
| UI 库 | Ant Design / MUI / Arco Design / TDesign |
| 构建 | Vite（Rollup / esbuild）/ webpack / Turbopack |
| 数据请求 | axios + 自封装 / TanStack Query / SWR |
| 代码规范 | ESLint + Prettier（eslint-plugin-react-hooks 必装） |

React 19 的主要新特性（了解即可，老版本均向下兼容演进）：

| 特性 | 说明 |
| --- | --- |
| Actions | 表单/异步动作内建处理 `pending`/`error` 状态 |
| `useActionState` / `useOptimistic` / `use` | 新 Hooks，简化异步状态与乐观更新 |
| ref 作为普通 prop | 函数组件可直接接收 `ref`，多数场景无需 `forwardRef` |
| `<Context>` 简写 | 直接用 `<Context value={...}>` 充当 Provider |
| Document Metadata | 组件内直接写 `<title>` / `<meta>` 会被提升到 `<head>` |
| 移除 UMD 构建 | CDN 引入需改用 ESM + importmap |

## 1.4 React 与 Vue 的设计差异

二者常被放在一起比较。理解差异能帮助你更好地使用 React：

| 维度 | React | Vue3 |
| --- | --- | --- |
| 定位 | UI 库，生态自由组合 | 渐进式框架，官方全家桶 |
| 模板 | JSX（JavaScript 表达式） | 模板语法 + 指令（`v-if`/`v-for`） |
| 数据驱动 | 显式 `setState` 触发重渲染 | Proxy 响应式自动追踪更新 |
| 数据流 | 单向 + 不可变数据 | 单向为主，`v-model` 语法糖支持双向 |
| 逻辑复用 | 自定义 Hook | 组合式函数（Composables） |
| 更新粒度 | 组件级重渲染（默认整棵子树） | 细粒度依赖追踪 |
| 跨层传值 | Context | provide / inject |

> 两者思想正逐步趋同：Vue3 的 `<script setup>` 越来越"函数式"，React 的 Hooks 与 Vue 的组合式函数本质同源。

## 1.5 单向数据流与不可变数据

React 两个最重要的设计约束：

- **单向数据流**：父组件通过 props 把数据传给子组件；子组件不能修改 props，只能通过回调（或状态管理）通知父组件修改。数据流向永远是自上而下的单行道，易于定位 bug。
- **不可变数据（Immutability）**：更新状态时不要修改原对象/数组，而是创建新的一份再赋值。React 依靠"引用是否变化"判断状态是否更新，因此必须替换引用。

```tsx
// 错误：原地修改，React 无法感知（引用没变）
state.user.age = 21

// 正确：创建新对象，替换引用
setUser({ ...user, age: 21 })
```

# 二、快速上手

## 2.1 方式一：CDN 引入（最简单）

React 19 移除了 UMD 构建，官方推荐在浏览器中使用原生 ESM + importmap 引入：

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8" />
  <title>React CDN Demo</title>
</head>
<body>
  <div id="root"></div>

  <!-- importmap：把裸模块名映射到 CDN 地址 -->
  <script type="importmap">
    {
      "imports": {
        "react": "https://esm.sh/react@19",
        "react-dom/client": "https://esm.sh/react-dom@19/client"
      }
    }
  </script>

  <script type="module">
    import { useState } from 'react'
    import { createRoot } from 'react-dom/client'

    function App() {
      const [count, setCount] = useState(0)
      return (
        <div>
          <h1>Hello React</h1>
          <button onClick={() => setCount(count + 1)}>count is {count}</button>
        </div>
      )
    }

    // 2. React 19：挂载必须使用 createRoot
    createRoot(document.getElementById('root')).render(<App />)
  </script>
</body>
</html>
```

注意：`esm.sh` / `unpkg` / `cdnjs` 等为第三方 CDN，生产环境建议锁定版本并自建镜像。

## 2.2 方式二：官方脚手架 Vite

`create-react-app`（CRA）已停止维护。现在新项目用 Vite 脚手架：

```
# npm
npm create vite@latest

# pnpm
pnpm create vite@latest
```

选择 React + TypeScript 模板：

```
✔ Select a framework: › React
✔ Select a variant: › TypeScript
```

或直接指定模板一步到位：

```
npm create vite@latest my-react-app -- --template react-ts
```

进入项目并启动：

```
cd my-react-app
npm install
npm run dev        # 开发服务器，默认 http://localhost:5173
npm run build      # 生产构建，输出 dist/
npm run preview    # 本地预览生产构建
```

> 说明：Vite 模板是纯前端 SPA。若需要 SSR / 全栈能力，可考虑 Next.js（`npx create-next-app@latest`）。

## 2.3 项目目录结构

```
my-react-app/
├── index.html              # 应用入口 HTML，包含 <div id="root">
├── package.json
├── vite.config.ts          # Vite 配置
├── tsconfig.json           # TypeScript 配置
├── eslint.config.js        # ESLint 配置
├── public/                 # 静态资源（原样拷贝，不走打包）
└── src/
    ├── main.tsx            # 入口文件，创建 Root 并渲染
    ├── App.tsx             # 根组件
    ├── index.css           # 全局样式
    ├── assets/             # 需要打包处理的资源（图片、样式）
    ├── components/         # 通用组件
    ├── hooks/              # 自定义 Hooks
    ├── layouts/            # 布局组件
    ├── pages/              # 页面级组件
    ├── router/             # 路由配置
    ├── store/              # 全局状态（Zustand / Redux）
    ├── api/                # 接口层
    ├── types/              # 类型定义
    └── utils/              # 工具函数
```

## 2.4 入口文件与渲染

`index.html` 中只需提供一个根节点：

```html
<div id="root"></div>
<script type="module" src="/src/main.tsx"></script>
```

`src/main.tsx`：

```tsx
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'
import './index.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>
)
```

- `createRoot().render()`：React 18+ 的挂载方式（替代旧的 `ReactDOM.render`）。
- `<StrictMode>`：开发模式下的严格检查，会故意"双调用"渲染函数与副作用，帮你暴露隐藏的 bug（正式构建无影响）。上线时保留即可。

## 2.5 第一个组件

组件就是一个返回 JSX 的普通函数。约定组件名以**大写字母开头**（否则会被当成普通 HTML 标签）：

```tsx
// src/components/Greeting.tsx
interface GreetingProps {
  name: string
  age?: number            // 可选
}

function Greeting({ name, age = 18 }: GreetingProps) {
  return (
    <div className="greeting">
      <h2>Hello, {name}</h2>
      {age !== undefined && <p>年龄：{age}</p>}
    </div>
  )
}

export default Greeting
```

```tsx
// src/App.tsx
import Greeting from './components/Greeting'

function App() {
  return (
    <div>
      <Greeting name="React" age={3} />
      <Greeting name="Vue" />
    </div>
  )
}

export default App
```

# 三、JSX 语法

> JSX 是 JavaScript 的语法扩展，看起来像 HTML，但本质是 `React.createElement` 的语法糖。它允许你在 JavaScript 中书写 UI，并把逻辑与结构放在一起。

## 3.1 插值表达式

在 JSX 中，用单花括号 `{表达式}` 嵌入任意 JavaScript 表达式：

```tsx
function Demo() {
  const message = 'Hello React'
  const count = 1
  const isOk = true
  const user = { name: '张三' }

  return (
    <div>
      {/* 文本插值：任意 JS 表达式 */}
      <p>{message}</p>
      <p>{count + 1}</p>
      <p>{isOk ? 'YES' : 'NO'}</p>
      <p>{message.toUpperCase()}</p>
      <p>{user.name}</p>

      {/* 对象不能直接渲染，需取属性 */}
      {/* <p>{user}</p>  会报错：Objects are not valid as a React child */}
    </div>
  )
}
```

注释写法：JSX 内用 `{/* ... */}`。

**渲染原始 HTML（危险，勿用于用户输入）**：JSX 默认会转义所有内容防止 XSS。确需渲染富文本时用 `dangerouslySetInnerHTML`（对应 Vue 的 `v-html`）：

```tsx
function Demo() {
  const rawHtml = '<strong style="color:red">bold</strong>'
  return <div dangerouslySetInnerHTML={{ __html: rawHtml }} />
}
```

## 3.2 属性绑定

与 Vue 的 `v-bind` 类似，JSX 中用 `{}` 动态绑定属性。注意一些属性名与 HTML 不同：

```tsx
function Demo() {
  const imgUrl = '/logo.png'
  const title = 'logo'
  const canSubmit = false
  const attrName = 'data-code'        // 原生 DOM 属性驼峰写

  return (
    <div>
      {/* class 要写成 className（class 是 JS 保留字） */}
      <div className="box">内容</div>

      {/* 布尔属性：disabled 等 */}
      <button disabled={!canSubmit}>提交</button>
      <input readOnly={true} />

      {/* style 必须传对象，属性名驼峰 */}
      <div style={{ color: '#4db6ac', fontSize: '16px', fontWeight: 'bold' }}>
        内联样式
      </div>

      {/* 动态属性名：属性名本身可以是变量（对象展开） */}
      <img src={imgUrl} alt={title} {...{ [attrName]: 'x' }} />

      {/* 展开 props：一次性传多个 */}
      <ChildComp {...{ a: 1, b: 2 }} />
    </div>
  )
}
```

字符串可以直接写：`<div className="box">` 中的 `"box"` 是字符串字面量；需要拼接/变量时用 `{...}`。

## 3.3 条件渲染

JSX 没有 `v-if` / `v-show` 指令，条件渲染就是 JavaScript 的条件表达式：

```tsx
function Demo() {
  const type = 'a'
  const visible = true
  const list: string[] = []
  const isLogged = false

  return (
    <div>
      {/* 方式一：三元表达式（if / else if / else） */}
      {type === 'a' ? <div>A</div> : type === 'b' ? <div>B</div> : <div>其他</div>}

      {/* 方式二：&& 短路（仅当条件为真时渲染） */}
      {isLogged && <span>欢迎回来</span>}

      {/* 方式三：|| 提供默认值 */}
      {list.length || <span>列表为空</span>}

      {/* 方式四：立即执行函数 / 抽离成变量再渲染 */}
      {(() => {
        if (type === 'a') return <div>A</div>
        if (type === 'b') return <div>B</div>
        return <div>其他</div>
      })()}

      {/* 对应 v-show：始终渲染，只切换 display 样式 */}
      <div style={{ display: visible ? 'block' : 'none' }}>始终在 DOM 中</div>
    </div>
  )
}
```

`v-if` 与 `&& / 三元` 的取舍（与 Vue 相同的心智模型）：

| Vue | React | 机制 | 适用场景 |
| --- | --- | --- | --- |
| `v-if` | `{cond && <Comp/>}` / 三元 | 条件为假时元素不渲染 | 很少切换、条件稳定 |
| `v-show` | 自行控制 `display` | 元素始终渲染 | 高频切换（如 Tab 页签） |

**注意**：`{cond && <Comp/>}` 中当 `cond` 是数字 `0` 时会渲染出 `0`，因此布尔判断建议写成 `{cond > 0 && ...}` 或 `!!cond && ...`。

## 3.4 列表渲染与 key

JSX 没有 `v-for`，用数组的 `map` 遍历生成一组元素（对应 Vue 的 `v-for`）：

```tsx
function Demo() {
  const items = [
    { id: 1, name: 'React' },
    { id: 2, name: 'Vue' },
    { id: 3, name: 'Svelte' }
  ]

  return (
    <ul>
      {items.map((item, index) => (
        <li key={item.id}>
          {index} - {item.name}
        </li>
      ))}
    </ul>
  )
}
```

**key 的重要性**：`key` 帮助 React 识别元素，决定哪些节点被复用、移动或重建。列表有增删、排序，或项内含有状态（输入框、勾选）时，必须给稳定的 `key`。

- ✅ 推荐：唯一且稳定的业务 id（`item.id`），或服务端主键。
- ❌ 避免：数组 `index`。增删/排序会让 React 错乱复用，出现内容错位、输入框状态串行。

> 与 Vue 完全一致：`key` 只需在兄弟节点间唯一即可，不用全局唯一。

范围遍历（1..10）可用 `Array.from` 或展开数组：

```tsx
{[...Array(10)].map((_, i) => <span key={i}>{i + 1}</span>)}
```

## 3.5 事件处理

用 `onXxx`（驼峰）绑定事件，值是一个函数。对应 Vue 的 `v-on` / `@`：

```tsx
function Demo() {
  // 直接绑定函数
  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    console.log('点击了', e)
  }

  return (
    <div>
      {/* 绑定方法：无需 this */}
      <button onClick={handleClick}>点击</button>

      {/* 需要传参：包一层箭头函数（不能直接写 handleClick(2) 否则立即执行） */}
      <button onClick={() => handleClick2(2)}>传参</button>

      {/* 阻止冒泡：对应 @click.stop */}
      <div onClick={onDivClick}>
        <button onClick={(e) => e.stopPropagation()}>不冒泡</button>
      </div>

      {/* 阻止默认行为：对应 @click.prevent */}
      <a href="https://example.com" onClick={(e) => e.preventDefault()}>
        链接
      </a>

      {/* 键盘事件：对应 @keyup.enter */}
      <input onKeyDown={(e) => {
        if (e.key === 'Enter') console.log('回车')
      }} />

      {/* 表单事件 */}
      <input onChange={(e) => console.log(e.target.value)} />
      <input onBlur={() => console.log('失焦')} />
    </div>
  )
}
```

要点：

- React 事件是**合成事件（SyntheticEvent）**，底层统一委托到根容器，事件对象是跨浏览器的封装；但 `e.target` 拿到的是真实 DOM。
- 事件对象是池化的（React 17 起已移除池化），异步回调里直接取 `e` 的字段是安全的（如 `setTimeout(() => console.log(e.target.value), 0)`）。
- 不需要手动 `removeEventListener`——这由框架管理。

## 3.6 表单：受控组件与非受控组件

React 没有 `v-model`。"值 + 事件"需要自己绑定，即**受控组件**：`value` 来自 state，用 `onChange` 回写 state。

```tsx
import { useState } from 'react'

function FormDemo() {
  const [msg, setMsg] = useState('')
  const [checked, setChecked] = useState(false)
  const [gender, setGender] = useState('男')
  const [selected, setSelected] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log({ msg, checked, gender, selected })
  }

  return (
    <form onSubmit={handleSubmit}>
      {/* input / textarea：等价于 :value + @input */}
      <input value={msg} onChange={(e) => setMsg(e.target.value)} />
      <textarea value={msg} onChange={(e) => setMsg(e.target.value)} />

      {/* checkbox：单个布尔 */}
      <input type="checkbox" checked={checked} onChange={(e) => setChecked(e.target.checked)} />

      {/* radio */}
      <input type="radio" name="gender" value="男" checked={gender === '男'}
             onChange={(e) => setGender(e.target.value)} />
      <input type="radio" name="gender" value="女" checked={gender === '女'}
             onChange={(e) => setGender(e.target.value)} />

      {/* select */}
      <select value={selected} onChange={(e) => setSelected(e.target.value)}>
        <option value="">请选择</option>
        <option value="a">A</option>
        <option value="b">B</option>
      </select>

      {/* 修饰符语义需要手动实现：
          .lazy -> onBlur / onChange 只提交；.number -> Number(e.target.value)；
          .trim -> e.target.value.trim() */}
      <input
        onChange={(e) => setMsg(e.target.value.trim())}
        onBlur={() => console.log('等价 v-model.lazy 提交', msg)}
      />

      <button type="submit">提交</button>
    </form>
  )
}
```

**非受控组件**：不把 value 交给 state，而是通过 ref 读取真实 DOM（对应 Vue 的 `ref` 拿表单值，适合简单一次性读取、文件上传等）：

```tsx
import { useRef } from 'react'

function Uncontrolled() {
  const inputRef = useRef<HTMLInputElement>(null)

  const submit = () => {
    console.log(inputRef.current?.value)
  }

  return (
    <div>
      {/* defaultValue 只在首次渲染生效 */}
      <input ref={inputRef} defaultValue="默认值" />
      <button onClick={submit}>读取值</button>
    </div>
  )
}
```

| Vue | React |
| --- | --- |
| `v-model="msg"` | `value` + `onChange`（受控组件） |
| `.number` / `.trim` 修饰符 | 手动转换 |
| `ref`（表单取值） | `useRef` 非受控组件 |

## 3.7 派生渲染数据（对应 computed）

React 没有 `computed`，但也不需要：**渲染函数本身就是"根据 state 计算后渲染"**。每次渲染直接计算即可，简单场景无需缓存：

```tsx
function Demo() {
  const [price, setPrice] = useState(100)
  const [discount, setDiscount] = useState(0.8)
  const [items, setItems] = useState([1, 2, 3, 4, 5])

  // 直接派生：每次渲染都重新计算（计算量小时完全没问题）
  const discountedPrice = price * discount
  const itemsTotal = items.reduce((s, n) => s + n, 0)
  const activeItems = items.filter(i => i % 2 === 0)   // 过滤/排序也同理

  return (
    <div>
      <p>原价：{price}</p>
      <p>折后价：{discountedPrice}</p>
      <p>总数：{itemsTotal}</p>
    </div>
  )
}
```

对应关系：

- 简单的派生值 → **直接计算**（首选，最简单）；
- 计算开销大、且希望跳过重复计算 → `useMemo`（见 5.4）；
- 希望"依赖变化时自动执行副作用"（对应 `watch`/`watchEffect`）→ `useEffect`（见 5.2）。

```tsx
// 对应 Vue 的 watch：
useEffect(() => {
  // 每当 price 或 discount 变化后执行
  console.log('价格参数变化了')
}, [price, discount])
```

# 四、State 与渲染机制

> 理解 React 的渲染机制，是写出正确代码的关键。记住两个核心结论：
> 1. **状态（state）变化才会触发重渲染**；直接改普通变量不会。
> 2. 一次渲染内的 state 值是"快照"，读取到的永远是本次渲染时的值。

## 4.1 useState 基础

```tsx
import { useState } from 'react'

function Counter() {
  const [count, setCount] = useState(0)     // 初始值

  const add = () => setCount(count + 1)     // 传新值

  const addTwice = () => {
    // 同一渲染内连续调用：count 仍是旧值，这样两次都基于旧值 => 只 +1
    setCount(count + 1)
    setCount(count + 1)                     // ❌ 结果仍是 +1
  }

  const addSafeTwice = () => {
    // 函数式更新：React 会按队列逐个应用 updater => 正确 +2
    setCount(c => c + 1)
    setCount(c => c + 1)                    // ✅ 结果 +2
  }

  return (
    <div>
      <p>{count}</p>
      <button onClick={add}>+1</button>
      <button onClick={addTwice}>+1(错误写法)</button>
      <button onClick={addSafeTwice}>+2</button>
    </div>
  )
}
```

要点：

- `useState` 返回 `[当前值, 更新函数]`，用数组解构（或自定义命名）接收。
- **每次渲染** `useState` 都会执行，但 React 会忽略初始值，返回当前最新 state。
- 更新函数传入"值"或"函数（基于上一次的值）"。需要连续累加、或值来自闭包时要优先用**函数式更新**。
- 状态提升时可用"惰性初始化"：`useState(() => expensiveInit())`，初始值只计算一次。

## 4.2 渲染机制与更新时机

React 的渲染（re-render）流程：

```
状态变化 → 触发渲染 → 重新调用组件函数生成新 UI（虚拟 DOM/Fiber）
        → 与上一次结果对比（reconciliation）→ 只更新真实 DOM 中变化的部分
```

触发组件重渲染的三种情况：

1. 组件自身 `setState`；
2. 父组件重渲染（默认子组件跟着重渲染）；
3. `context` 值变化（消费该 context 的组件重渲染）。

**state 更新是异步且批量（batching）的**：

```tsx
function BatchDemo() {
  const [a, setA] = useState(0)
  const [b, setB] = useState(0)

  const handleClick = async () => {
    // React 18+ 自动批处理：事件、Promise、setTimeout 内都会合并
    setA(a => a + 1)
    setB(b => b + 1)        // 多次 setState 合并为一次渲染
    console.log(a)          // 仍是旧值 0（本次渲染还没结束）
  }

  return <button onClick={handleClick}>a:{a} b:{b}</button>
}
```

**快照模型**：组件函数的每次调用，看到的 state 都是"本次渲染那一刻"的值。下面的例子里，点击后 alert 的是旧值：

```tsx
function Snapshot() {
  const [count, setCount] = useState(0)

  const show = () => {
    setTimeout(() => alert(count), 3000)   // 弹的是点击瞬间的 count
  }

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>+1</button>
      <button onClick={show}>3秒后查看 count</button>
    </div>
  )
}
```

若希望拿"最新值"，用 ref 或函数式更新，而不是依赖闭包中的旧 state。

## 4.3 不可变更新：对象与数组

**永远不要直接修改（mutate）state 中的对象/数组**，要创建新引用：

```tsx
function StateUpdate() {
  const [user, setUser] = useState({ name: '张三', age: 20 })
  const [list, setList] = useState<number[]>([1, 2, 3])
  const [todos, setTodos] = useState([
    { id: 1, text: '学习 React', done: false }
  ])

  // 对象：浅拷贝后覆盖
  const updateName = () => setUser({ ...user, name: '李四' })
  const updateNested = () =>
    setUser(prev => ({ ...prev, address: { ...prev.address, city: '北京' } }))

  // 数组：map / filter / 展开 生成新数组
  const push = () => setList(prev => [...prev, prev.length + 1])
  const remove = () => setList(prev => prev.filter((_, i) => i !== 0))
  const mapIt = () => setList(prev => prev.map(n => n * 2))
  const sort = () => setList(prev => [...prev].sort((a, b) => b - a))  // 先拷贝再排序

  // 对象数组中的单项更新：map 定位替换
  const toggleTodo = (id: number) =>
    setTodos(prev => prev.map(t => t.id === id ? { ...t, done: !t.done } : t))

  return <div>{/* ... */}</div>
}
```

数组常用更新速查：

| 操作 | 不可变写法 |
| --- | --- |
| 追加 | `[...arr, item]` |
| 头部插入 | `[item, ...arr]` |
| 删除 | `arr.filter(it => it.id !== id)` |
| 更新某项 | `arr.map(it => it.id === id ? { ...it, x: 1 } : it)` |
| 排序 | `[...arr].sort(cmp)`（先拷贝） |
| 清空 | `[]` |

> 深层嵌套修改很繁琐时，推荐引入 `immer`（`npm i immer`，Redux Toolkit 内部也用它），可以直接写"看似可变"的代码。

## 4.4 useReducer

复杂状态（多种 action、对象状态互相关联）用 `useReducer` 更清晰。它像 Vuex/Pinia 中"mutation 汇总"的迷你版：

```tsx
import { useReducer } from 'react'

interface State { count: number }
type Action = { type: 'inc'; step?: number } | { type: 'reset' }

function reducer(state: State, action: Action): State {
  switch (action.type) {
    case 'inc':   return { count: state.count + (action.step ?? 1) }
    case 'reset': return { count: 0 }
    default:      return state
  }
}

function Counter() {
  const [state, dispatch] = useReducer(reducer, { count: 0 })

  return (
    <div>
      <p>{state.count}</p>
      <button onClick={() => dispatch({ type: 'inc' })}>+1</button>
      <button onClick={() => dispatch({ type: 'inc', step: 5 })}>+5</button>
      <button onClick={() => dispatch({ type: 'reset' })}>重置</button>
    </div>
  )
}
```

**useState vs useReducer**：

| | useState | useReducer |
| --- | --- | --- |
| 适用 | 简单、独立的 state | 多个互相关联的字段、复杂更新逻辑 |
| 更新方式 | 直接传新值 | dispatch(action) → reducer 集中处理 |
| 可读性 | 简单直观 | action 语义清晰、易测试/易追踪 |

## 4.5 状态提升与单向数据流

当多个组件需要共享同一份状态时，把状态**提升**到它们最近的共同父组件，父组件通过 props 下发值、通过回调下发修改能力：

```tsx
import { useState } from 'react'

// 子组件：只负责展示 + 上报事件（不知道自己被谁管理）
function Child({ label, value, onChange }: {
  label: string
  value: string
  onChange: (v: string) => void
}) {
  return (
    <label>
      {label}
      <input value={value} onChange={(e) => onChange(e.target.value)} />
    </label>
  )
}

// 父组件：状态唯一来源（Single Source of Truth）
function Parent() {
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')

  return (
    <div>
      <Child label="姓" value={firstName} onChange={setFirstName} />
      <Child label="名" value={lastName} onChange={setLastName} />
      <p>全名：{firstName}{lastName}</p>
    </div>
  )
}
```

单向数据流让"状态从哪来、谁修改它"一目了然。状态放哪的建议：

1. 只在本组件用 → 放本组件；
2. 父子都要用 → 提升到父组件；
3. 很多无关组件要用 / 跨多层 → 用 Context 或全局状态库（见六、九章）。

# 五、Hooks 详解

## 5.1 Hooks 是什么与使用规则

Hook 是 React 16.8+ 提供的一组函数，让函数组件"钩住"状态与副作用。内置常用 Hooks：

| Hook | 作用 | 对应/对标 |
| --- | --- | --- |
| `useState` | 声明状态 | `ref` |
| `useEffect` | 处理副作用（请求、订阅、DOM 操作） | `watch` / 生命周期钩子 |
| `useRef` | DOM 引用 / 存可变值 | `ref`（DOM）/ 普通变量 |
| `useMemo` | 缓存计算结果 | `computed` |
| `useCallback` | 缓存函数引用 | - |
| `useContext` | 读取 Context | `inject` |
| `useReducer` | 复杂状态管理 | - |
| `useLayoutEffect` | 同步于 DOM 变更后的副作用 | - |
| `useId` | 生成稳定唯一 id（无障碍） | - |

**Hooks 使用铁律**（eslint-plugin-react-hooks 会强制检查）：

1. **只能在函数组件 / 自定义 Hook 的顶层调用**：不要在 `if`、`for`、普通函数、回调里调用 Hooks。
2. **不能在类组件中使用**（类组件用生命周期）。

原因：React 依赖 Hooks 的**调用顺序**把每次渲染的 state 关联起来。若条件调用导致顺序变化，state 就会错乱。

```tsx
// ❌ 错误：条件调用 Hooks
if (visible) {
  const [a] = useState(0)
}

// ✅ 正确：Hooks 恒在顶层，条件放在逻辑里
const [a] = useState(0)
if (visible) { /* 使用 a */ }
```

## 5.2 useEffect 与"生命周期"

`useEffect` 让函数组件表达副作用。它在**渲染完成后**执行，且不会阻塞浏览器绘制：

```tsx
import { useState, useEffect } from 'react'

function Demo() {
  const [count, setCount] = useState(0)
  const [keyword, setKeyword] = useState('')

  // 1. 依赖数组为空：仅在挂载后执行一次（对标 onMounted）
  useEffect(() => {
    document.title = '页面加载'
    // 返回清理函数：卸载前执行（对标 onBeforeUnmount）
    return () => console.log('组件卸载')
  }, [])

  // 2. 带依赖：count 变化后执行（对标 watch + immediate）
  useEffect(() => {
    console.log('count 变化为', count)
  }, [count])

  // 3. 不写依赖数组：每次渲染后都执行（慎用，通常不需要）
  useEffect(() => {
    console.log('每次渲染都跑')
  })

  // 4. 副作用清理：订阅 / 定时器必须清理，防止内存泄漏
  useEffect(() => {
    const timer = setInterval(() => console.log('tick'), 1000)
    return () => clearInterval(timer)     // 卸载 或 依赖变化重跑前 清理
  }, [])

  // 5. 典型的"防抖请求"组合：keyword 停止变化 300ms 后发请求
  useEffect(() => {
    if (!keyword) return
    const timer = setTimeout(() => {
      fetch(`/api/search?q=${keyword}`)
    }, 300)
    return () => clearTimeout(timer)      // 清理上一次的定时器
  }, [keyword])

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>+1</button>
      <input value={keyword} onChange={(e) => setKeyword(e.target.value)} />
    </div>
  )
}
```

**依赖数组（deps）语义速查**：

| 写法 | 执行时机 | 对标 Vue |
| --- | --- | --- |
| `useEffect(fn)` | 每次渲染后 | `watchEffect` |
| `useEffect(fn, [])` | 仅挂载后执行一次，卸载时执行清理 | `onMounted` / `onBeforeUnmount` |
| `useEffect(fn, [a, b])` | `a` 或 `b` 变化后执行（首轮也执行） | `watch(() => [a, b], ...)` |
| 返回 cleanup | 卸载时 / 下一次 effect 执行前 | `onCleanup` |

**常见坑**：

- 依赖数组里的变量必须写全（若用到了 state，漏写会读到过期值）。ESLint 的 `exhaustive-deps` 规则会提示。
- 依赖若是"每次渲染都新建的对象/函数"，会造成 effect 反复执行（见 5.4 的 useMemo/useCallback 场景）。
- **不要在 effect 里同步改 state 导致死循环**：若 effect 依赖 `x`，又 `setX(...)`，每次更新又触发 effect，就会无限循环。

`useLayoutEffect` 与 `useEffect` 的区别：前者在 DOM 变更后、浏览器绘制前**同步**执行，适合"需要先量测再更新布局"（如根据容器宽度计算位置），避免闪烁；`useEffect` 在绘制后执行，适合绝大多数场景。

## 5.3 useRef

`useRef` 返回一个固定的 `{ current: any }` 容器，用途有二：

1. **访问 DOM**（对标 Vue 的模板 ref）；
2. **保存可变值**：修改 `ref.current` 不会触发渲染，可存放"最新值"或"不需要驱动 UI 的数据"。

```tsx
import { useRef, useEffect } from 'react'

function UseRefDemo() {
  // 1. DOM 引用
  const inputRef = useRef<HTMLInputElement>(null)

  // 2. 保存可变值（每次渲染同一个对象，改变它不触发渲染）
  const countRef = useRef(0)

  useEffect(() => {
    inputRef.current?.focus()       // 挂载后自动聚焦
  }, [])

  const onClick = () => {
    countRef.current += 1
    console.log('点击次数(不触发渲染):', countRef.current)
  }

  // 3. 跨闭包拿"最新值"：把最新 count 同步进 ref
  const [count, setCount] = useState(0)
  const latestCount = useRef(count)
  latestCount.current = count       // 每次渲染都同步（渲染期间写 ref 可接受）

  return (
    <div>
      <input ref={inputRef} placeholder="自动聚焦" />
      <button onClick={onClick}>累计</button>
      <p>count: {count}</p>
    </div>
  )
}
```

> 小结：需要"影响界面"用 `useState`；需要"读 DOM / 存不驱动 UI 的数据"用 `useRef`。

## 5.4 useMemo 与 useCallback

`useMemo` 缓存计算结果（对标 computed 的缓存）；`useCallback` 缓存函数引用。**二者都是优化手段，不是必须**——先写出正确代码，测量确认有性能问题再引入。

```tsx
import { useMemo, useCallback, useState } from 'react'

function ExpensiveList({ keyword, onPick }: {
  keyword: string
  onPick: (item: number) => void
}) {
  // useMemo：只有当 keyword / 依赖变化时才重新计算
  const bigData = useMemo(() => {
    // 模拟耗时计算：生成 10000 条过滤后的数据
    const arr: number[] = []
    for (let i = 0; i < 10000; i++) {
      if (String(i).includes(keyword)) arr.push(i)
    }
    return arr
  }, [keyword])

  return (
    <ul>
      {bigData.map(n => (
        <li key={n} onClick={() => onPick(n)}>{n}</li>
      ))}
    </ul>
  )
}

function Parent() {
  const [keyword, setKeyword] = useState('')
  const [other, setOther] = useState(0)

  // useCallback：缓存函数，保证引用稳定，避免子组件因"新函数"而重渲染
  const handlePick = useCallback((n: number) => {
    console.log('选中', n)
  }, [])

  return (
    <div>
      <input value={keyword} onChange={(e) => setKeyword(e.target.value)} />
      {/* other 变化导致 Parent 重渲染时，只要 keyword 没变，ExpensiveList 不会重算 */}
      <ExpensiveList keyword={keyword} onPick={handlePick} />
      <button onClick={() => setOther(other + 1)}>无关 state: {other}</button>
    </div>
  )
}
```

**关键认知**：

- `useMemo` / `useCallback` 都需声明依赖数组；依赖变化才重新计算/重建。
- 函数组件每次渲染都会**重新创建普通对象与函数**。若传给被 `React.memo` 包裹的子组件，父组件每次渲染子组件都会跟着渲染——用 `useCallback` 稳定引用可打破这一点（配合 10.1 的 memo）。
- **不要滥用**：缓存本身有开销。简单计算直接算（见 3.7）；只有计算昂贵、或作为 memo 子组件的 props 时才需要。

## 5.5 自定义 Hook（Custom Hooks）

自定义 Hook 就是把"有状态的逻辑"抽成一个以 `use` 开头的函数，是函数组件时代**逻辑复用的主方式**（对标 Vue 的组合式函数 Composables）。它内部可以自由使用其它 Hooks。

示例：useMouse 鼠标坐标监听

```tsx
// src/hooks/useMouse.ts
import { useEffect, useState } from 'react'

export function useMouse() {
  const [position, setPosition] = useState({ x: 0, y: 0 })

  useEffect(() => {
    const update = (e: MouseEvent) => setPosition({ x: e.pageX, y: e.pageY })
    window.addEventListener('mousemove', update)
    return () => window.removeEventListener('mousemove', update)
  }, [])

  return position
}
```

```tsx
// 组件中使用
import { useMouse } from '@/hooks/useMouse'

function Cursor() {
  const { x, y } = useMouse()
  return <span>鼠标位置: {x}, {y}</span>
}
```

自定义 Hook 命名与最佳实践：

- 命名以 `use` 开头（React 靠这个前缀识别 Hook 调用，违反会触发 lint 报错）；
- 单一职责，一个 Hook 只做一件事；
- 参数可传外部值，返回值可与其它 Hook 自由组合；
- 返回值尽量结构化（返回对象/数组），方便解构；
- 内部可安全使用 `useEffect` 等，副作用随调用它的组件生命周期走。

示例：防抖值与通用请求 Hook

```tsx
// src/hooks/useDebounceValue.ts
import { useState, useEffect } from 'react'

export function useDebounceValue<T>(value: T, delay = 300) {
  const [debounced, setDebounced] = useState(value)

  useEffect(() => {
    const timer = setTimeout(() => setDebounced(value), delay)
    return () => clearTimeout(timer)
  }, [value, delay])

  return debounced
}
```

```tsx
// src/hooks/useAsyncData.ts
import { useEffect, useState } from 'react'

export function useAsyncData<T>(fetcher: () => Promise<T>) {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  async function run() {
    setLoading(true)
    setError(null)
    try {
      setData(await fetcher())
    } catch (e) {
      setError(e as Error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    run()
  }, [])          // 简单场景挂载时执行一次；复杂场景把 fetcher 提升为参数并加依赖

  return { data, loading, error, reload: run }
}
```

## 5.6 其它常用 Hooks 速览

```tsx
import {
  useId, useTransition, useDeferredValue, useLayoutEffect
} from 'react'

// useId：生成稳定且唯一的 id（用于 label/input 关联，避免拼接随机数）
function Field() {
  const id = useId()
  return (
    <div>
      <label htmlFor={id}>用户名</label>
      <input id={id} />
    </div>
  )
}

// useTransition：标记"低优先级更新"，让输入框保持流畅
function Search() {
  const [keyword, setKeyword] = useState('')
  const [isPending, startTransition] = useTransition()

  const handleChange = (v: string) => {
    setKeyword(v)                       // 高优先级：输入框立刻响应
    startTransition(() => setResult(v)) // 低优先级：结果列表可延迟更新
  }
  return <div>{isPending ? '更新中…' : null}</div>
}

// useDeferredValue：直接拿"延迟后的值"，适合大量数据过滤场景
function Filter({ list }: { list: number[] }) {
  const [kw, setKw] = useState('')
  const deferredKw = useDeferredValue(kw)
  const filtered = list.filter(n => String(n).includes(deferredKw))
  return (
    <div>
      <input value={kw} onChange={(e) => setKw(e.target.value)} />
      <ul>{filtered.map(n => <li key={n}>{n}</li>)}</ul>
    </div>
  )
}
```

React 19 还新增了 `useActionState`（表单动作状态）、`useOptimistic`（乐观更新）、`use(resource)`（直接读取 Promise / Context）等，多用于 Next.js / 数据框架，进阶后按需查阅官方文档。

# 六、组件通信

> 组件是 React 应用的基本单元。组件间通信与 Vue 一样是高频面试点，也是日常开发核心。React 的通信整体围绕"单向数据流"展开，手段更少但更明确。

## 6.1 组件通信方式总览

| 方式 | 方向 | 适用场景 | 对标 Vue |
| --- | --- | --- | --- |
| `props` | 父 -> 子 | 父传数据给子 | `props` |
| 回调函数 | 子 -> 父 | 子通知父 / 传值 | `emit` |
| `Context` | 祖先 -> 后代 | 跨层级共享，无需逐层传 | `provide / inject` |
| `ref` + `useImperativeHandle` | 父访问子 | 父调用子内部方法 | `defineExpose` |
| `children` / 组合 props | 父 -> 子 | 父向子传入模板/渲染内容 | Slot 插槽 |
| 状态提升 | 兄弟共享 | 提升到共同父组件 | 同左 |
| 全局状态库 | 任意 | 跨大量无关组件 | Pinia |
| URL / 路由参数 | 任意 | 页面间传参 | 路由参数 |

## 6.2 父传子：props

```tsx
// 子组件 Child.tsx
interface UserInfo { id: number; name: string }

interface ChildProps {
  msg: string
  user: UserInfo
  count: number
  visible?: boolean          // 可选
  defaultText?: string       // 带默认值（解构默认值）
  onUpdate?: (val: string) => void    // 函数类型 props 也在此声明
}

// 解构接收 props；props 是只读的，严禁子组件直接修改
function Child({ msg, user, count, visible = false, defaultText = '默认' }: ChildProps) {
  return (
    <div>
      {msg} - {user.name} - {count} - {visible} - {defaultText}
    </div>
  )
}
```

```tsx
// 父组件 Parent.tsx
import { useState } from 'react'
import Child from './Child'

function Parent() {
  const [n, setN] = useState(10)

  const handleUpdate = (val: string) => console.log('子组件通知:', val)

  return (
    <Child
      msg="来自父组件的数据"
      user={{ id: 1, name: '张三' }}
      count={n}
      onUpdate={handleUpdate}
    />
  )
}
```

- 类型定义 + 解构默认值是 React + TS 的标准姿势（对标 `withDefaults` + 泛型 `defineProps`）。
- props 不可变：需要改就通过回调上报父组件，由"状态的拥有者"去改。

## 6.3 子传父：回调函数

子组件不需要"声明事件"，直接把**修改能力通过 props 下发**即可。父组件传函数，子组件在合适时机调用并携带参数：

```tsx
// 子组件 Child.tsx
interface ChildProps {
  onSubmit: (payload: { ok: boolean }) => void
  onValueChange: (value: string) => void
}

function Child({ onSubmit, onValueChange }: ChildProps) {
  return (
    <div>
      <button onClick={() => onSubmit({ ok: true })}>提交</button>
      <input onChange={(e) => onValueChange(e.target.value)} />
    </div>
  )
}
```

```tsx
// 父组件 Parent.tsx
function Parent() {
  return (
    <Child
      onSubmit={(payload) => console.log('submit', payload)}
      onValueChange={(v) => console.log('输入了', v)}
    />
  )
}
```

> 子组件"通知父组件并传值"在 React 里就是"调用父组件下发的回调函数"，等价于 Vue 的 `emit('event', payload)`。

## 6.4 跨层级传值：Context

Context 用于在组件树中跨层级共享数据（主题、语言、当前用户等），避免 props 逐层透传。对标 Vue 的 `provide / inject`。

```tsx
// src/context/ThemeContext.ts
import { createContext, useContext } from 'react'

export interface ThemeCtx {
  theme: 'light' | 'dark'
  toggle: () => void
}

// 1. createContext：可给默认值（也可为 undefined 再自行校验）
export const ThemeContext = createContext<ThemeCtx | null>(null)

// 2. 封装成 Hook：强制在 Provider 内使用，类型安全
export function useTheme() {
  const ctx = useContext(ThemeContext)
  if (!ctx) throw new Error('useTheme 必须在 ThemeProvider 内使用')
  return ctx
}
```

```tsx
// 顶层提供方 App.tsx
import { useState } from 'react'
import { ThemeContext } from '@/context/ThemeContext'

function App() {
  const [theme, setTheme] = useState<'light' | 'dark'>('light')

  // value 建议用 useMemo 包裹，避免父组件每次渲染都新建对象导致消费者全量重渲染
  const value = {
    theme,
    toggle: () => setTheme(t => (t === 'light' ? 'dark' : 'light'))
  }

  return (
    <ThemeContext.Provider value={value}>
      <Toolbar />
      <MainContent />
    </ThemeContext.Provider>
  )
}
```

```tsx
// 任意深度的后代使用
import { useTheme } from '@/context/ThemeContext'

function Toolbar() {
  const { theme, toggle } = useTheme()
  return (
    <button onClick={toggle}>
      当前主题: {theme}（点击切换）
    </button>
  )
}
```

Context 使用要点：

- 一个 Context 只放"同主题"的数据，多个 Context 可嵌套（拆细避免无关组件被牵连重渲染）；
- **值变化会让所有消费该 Context 的组件重渲染**：value 尽量 memo 化（`useMemo`），并把 Provider 放上层；
- 默认值只在"找不到 Provider"时生效；更稳妥的是像上面那样抛错提醒。
- React 19 简写：`<ThemeContext value={value}>` 直接充当 Provider，无需 `.Provider`。

## 6.5 父访问子组件：ref + useImperativeHandle

父组件要"命令式"调用子组件的方法（如让子组件聚焦、清空、刷新）时，用 `forwardRef` + `useImperativeHandle` 暴露受控的方法（对标 `defineExpose`）：

```tsx
// 子组件 FancyInput.tsx
import { forwardRef, useImperativeHandle, useRef } from 'react'

export interface FancyInputHandle {
  focus: () => void
  clear: () => void
}

// 第一个泛型是"暴露给父组件的方法句柄"，第二个是 props
const FancyInput = forwardRef<FancyInputHandle, { placeholder?: string }>(
  (props, ref) => {
    const inputRef = useRef<HTMLInputElement>(null)

    // 只暴露你愿意暴露的能力（子组件内部默认对外封闭）
    useImperativeHandle(ref, () => ({
      focus: () => inputRef.current?.focus(),
      clear: () => {
        if (inputRef.current) inputRef.current.value = ''
      }
    }))

    return <input ref={inputRef} placeholder={props.placeholder} />
  }
)

export default FancyInput
```

```tsx
// 父组件 Parent.tsx
import { useRef } from 'react'
import FancyInput, { type FancyInputHandle } from './FancyInput'

function Parent() {
  const childRef = useRef<FancyInputHandle>(null)

  return (
    <div>
      <FancyInput ref={childRef} placeholder="请输入" />
      <button onClick={() => childRef.current?.focus()}>聚焦</button>
      <button onClick={() => childRef.current?.clear()}>清空</button>
    </div>
  )
}
```

> React 19 起 `ref` 可作为普通 prop 传入函数组件（多数场景可省去 `forwardRef`），写法演进后更简单。

## 6.6 组合：children 与"插槽"模式

React 用**组件组合（Composition）**代替模板插槽。`children` 即"包裹在子组件内部的内容"（对标默认插槽）：

```tsx
// Card.tsx：接收 children
function Card({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="card">
      <div className="card-title">{title}</div>
      <div className="card-body">{children}</div>
    </div>
  )
}
```

```tsx
// 使用：children 就是默认插槽内容
function Demo() {
  return (
    <Card title="卡片标题">
      <p>这是主体内容</p>
    </Card>
  )
}
```

**"具名插槽"与"作用域插槽"**：React 用 props 传组件/函数实现。给子组件传"JSX"就是具名插槽；传"函数（接收子组件数据、返回 JSX）"就是作用域插槽（Render Props）：

```tsx
// List.tsx：renderItem 函数接收子组件内部数据 -> 作用域插槽
interface ListProps<T> {
  items: T[]
  renderItem: (item: T, index: number) => React.ReactNode
}

function List<T>({ items, renderItem }: ListProps<T>) {
  return (
    <ul>
      {items.map((item, index) => (
        <li key={index}>{renderItem(item, index)}</li>
      ))}
    </ul>
  )
}
```

```tsx
// 父组件使用：数据在子组件里，渲染方式由父组件决定
function Demo() {
  const items = ['React', 'Vue', 'Svelte']
  return (
    <List
      items={items}
      renderItem={(item, index) => <span>{index}: {item}</span>}
    />
  )
}
```

| Vue 插槽 | React 等价写法 |
| --- | --- |
| 默认插槽 `<slot/>` | `props.children` |
| 具名插槽 `<slot name="footer"/>` | 传 props：`<Layout footer={<div/>}>` |
| 作用域插槽 | 函数 props / Render Props：`renderItem={(item) => ...}` |

## 6.7 状态提升与通信决策

当 state 需要在多处共享时，遵循"状态放哪"决策树：

1. 只在一个组件内部用 → 留在本组件；
2. 父子都要用 → **状态提升**到父组件，props 下发 + 回调上抛；
3. 祖先与深层后代共享 → **Context**；
4. 页面/模块级大量共享、逻辑复杂、需要持久化与调试工具 → **全局状态库**（Redux Toolkit / Zustand）。

通信原则：

- 优先 props + 回调（数据流最清晰）；
- Context 适合低频变更的"配置类"数据，高频变更需谨慎拆分；
- 尽量避免"跨组件共享一份会被多处随意改写的全局数据"——保持修改点收敛。

# 七、工程化：TypeScript + Vite

## 7.1 Vite 与 TypeScript 基础配置

```ts
// vite.config.ts
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],        // JSX 编译 + 开发热更新
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
          'react-vendor': ['react', 'react-dom', 'react-router-dom'],  // 手动分包
          'antd': ['antd']
        }
      }
    }
  }
})
```

需要别名对应到 `tsconfig.json`：

```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": { "@/*": ["src/*"] }
  }
}
```

环境变量（同 Vite 规范，须以 `VITE_` 前缀暴露给客户端）：

```
# .env.development
VITE_APP_TITLE=dev环境
VITE_API_BASE=/api

# .env.production
VITE_APP_TITLE=prod环境
VITE_API_BASE=https://api.example.com
```

```ts
// 使用环境变量
const apiBase = import.meta.env.VITE_API_BASE
console.log(import.meta.env.MODE)   // development / production
```

## 7.2 TypeScript 基础类型实践

```tsx
import { useState } from 'react'

// 组件 props 类型
interface UserInfo { id: number; name: string }

interface UserCardProps {
  user: UserInfo
  tags?: string[]          // 可选
  onEdit: (user: UserInfo) => void
}

// 子组件
function UserCard({ user, tags = [], onEdit }: UserCardProps) {
  return (
    <div onClick={() => onEdit(user)}>
      {user.name}
      {tags.map(t => <span key={t}>{t}</span>)}
    </div>
  )
}

// 父组件：useState 泛型推断
function App() {
  const [user, setUser] = useState<UserInfo | null>(null)

  // 事件对象类型
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    console.log(e.target.value)
  }
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
  }

  // 引用类型：RefObject
  const divRef = useRef<HTMLDivElement>(null)

  return <div ref={divRef}>{/* ... */}</div>
}
```

常用 React 类型：

```tsx
import type { ReactNode, FC, CSSProperties, ChangeEvent, FormEvent, MouseEvent } from 'react'

// ReactNode：任意可渲染内容（children 类型）
const node: ReactNode = <div>任意内容</div>

// FC / FunctionComponent：带 children 的标准函数组件类型
const Card: FC<{ title: string }> = ({ title, children }) => (
  <div>{title}{children}</div>
)

// 样式对象类型
const style: CSSProperties = { color: '#4db6ac' }
```

## 7.3 Hooks + TS 泛型

```tsx
// 泛型封装请求
export function useRequest<T>(fetcher: () => Promise<T>, deps: unknown[] = []) {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    fetcher()
      .then((res) => { if (!cancelled) setData(res) })
      .finally(() => { if (!cancelled) setLoading(false) })
    return () => { cancelled = true }   // 卸载 / deps 变化时取消旧结果
  }, deps)

  return { data, loading }
}

// 使用：类型自动推导
const { data, loading } = useRequest<UserInfo>(() => getUser(1))
```

# 八、路由 React Router

## 8.1 安装与基本配置

```
# v6 及历史版本
npm install react-router-dom

# v7 起包名统一（功能向下兼容，以下示例均可运行）
npm install react-router
```

现代推荐使用 **数据路由**（`createBrowserRouter` + `<RouterProvider>`），路由即数据，支持 loader/懒加载/错误处理：

```tsx
// src/router/index.tsx
import { createBrowserRouter } from 'react-router-dom'
import Layout from '@/layouts/Layout'
import Home from '@/pages/Home'
import RequireAuth from '@/components/RequireAuth'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,          // 父路由：负责布局 + <Outlet/>
    children: [
      { index: true, element: <Home /> },                     // 默认子路由
      { path: 'about', element: <About /> },
      { path: 'user/:id', element: <UserDetail /> },          // 动态参数
      {
        path: 'admin',
        element: (
          <RequireAuth>
            <Admin />
          </RequireAuth>
        )
      }
    ]
  },
  { path: 'login', element: <Login /> },
  { path: '*', element: <NotFound /> }                        // 404 兜底
])

export default router
```

```tsx
// 入口挂载 src/main.tsx
import { RouterProvider } from 'react-router-dom'
import router from '@/router'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
)
```

`createBrowserRouter` 使用 History API（`history: 'browser'`）；需要 hash 模式可改 `createHashRouter`。

## 8.2 声明式导航与路由出口

```tsx
// layouts/Layout.tsx：父路由渲染布局 + <Outlet/>（子路由出口）
import { Link, NavLink, Outlet } from 'react-router-dom'

function Layout() {
  return (
    <div className="layout">
      <nav>
        {/* 普通链接 */}
        <Link to="/">首页</Link>
        {/* NavLink：自动带 active 状态 */}
        <NavLink
          to="/about"
          className={({ isActive }) => (isActive ? 'active' : '')}
        >
          关于
        </NavLink>
        <NavLink to="/user/123">用户详情</NavLink>
      </nav>

      {/* 子路由渲染位置（对应 <router-view/>） */}
      <main><Outlet /></main>
    </div>
  )
}
```

## 8.3 编程式导航与路由信息

```tsx
import { useNavigate, useParams, useSearchParams, useLocation } from 'react-router-dom'

function UserPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { id } = useParams<{ id: string }>()            // /user/:id -> { id }
  const [searchParams, setSearchParams] = useSearchParams()   // ?q=xx

  const goHome = () => navigate('/')
  const goReplace = () => navigate('/about', { replace: true })   // 不产生历史
  const goBack = () => navigate(-1)

  // 携带 query / state
  const goSearch = () => navigate('/search?q=react')
  const goWithState = () => navigate('/detail', { state: { from: 'list' } })

  console.log(id)                       // 路由参数
  console.log(searchParams.get('q'))    // query 参数
  console.log(location.pathname)        // /user/123
  console.log(location.state)           // 编程式导航携带的状态

  return <div>{/* ... */}</div>
}
```

**同一路由参数变化时组件会复用，需要手动响应**（对应 Vue `watch route.params`）：

```tsx
import { useEffect } from 'react'
import { useParams } from 'react-router-dom'

function UserDetail() {
  const { id } = useParams()

  // id 变化时重新请求（组件实例复用，不会重新挂载）
  useEffect(() => {
    fetchUser(id!)
  }, [id])

  return <div>用户 {id}</div>
}
```

## 8.4 嵌套路由

与 Vue 一样通过 `children` 表达嵌套，父组件渲染 `<Outlet/>`：

```tsx
const router = createBrowserRouter([
  {
    path: '/user',
    element: <UserLayout />,          // 内含 <Outlet/>
    children: [
      { index: true, element: <UserHome /> },          // 访问 /user 渲染
      { path: 'profile', element: <UserProfile /> },   // /user/profile
      { path: 'settings', element: <UserSettings /> }  // /user/settings
    ]
  }
])
```

```tsx
// UserLayout.tsx
import { Outlet } from 'react-router-dom'

function UserLayout() {
  return (
    <div className="user-layout">
      <SideMenu />
      <div className="content"><Outlet /></div>
    </div>
  )
}
```

## 8.5 路由守卫（登录校验）

React Router 没有 Vue 那种全局 `beforeEach` 守卫，常见的做法是**封装一个"守卫组件"**包裹受保护页面：

```tsx
// components/RequireAuth.tsx
import { Navigate, useLocation } from 'react-router-dom'
import type { ReactNode } from 'react'

function RequireAuth({ children }: { children: ReactNode }) {
  const token = localStorage.getItem('token')
  const location = useLocation()

  if (!token) {
    // 未登录：重定向到登录页，并记录来源，登录后跳回
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }
  return <>{children}</>
}

export default RequireAuth
```

```tsx
// pages/Login.tsx：登录成功后跳回原页面
import { useLocation, useNavigate } from 'react-router-dom'

function Login() {
  const navigate = useNavigate()
  const location = useLocation()

  const doLogin = () => {
    localStorage.setItem('token', 'xxx')
    const from = (location.state as { from?: string })?.from ?? '/'
    navigate(from, { replace: true })
  }

  return <button onClick={doLogin}>登录</button>
}
```

设置页面标题、埋点等"全局后置逻辑"，可抽一个订阅路由变化的小 Hook：

```tsx
import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'

export function usePageTitle(titleMap: Record<string, string>) {
  const location = useLocation()
  useEffect(() => {
    document.title = titleMap[location.pathname] ?? '我的站点'
    // 埋点上报 location.pathname
  }, [location.pathname])
}
```

## 8.6 懒加载与代码分包

路由级懒加载用 `React.lazy` + `<Suspense>`（对应 `defineAsyncComponent`）：

```tsx
// 方式一：React.lazy 包一层
import { lazy, Suspense } from 'react'

const About = lazy(() => import('@/pages/About'))
const UserDetail = lazy(() => import('@/pages/UserDetail'))

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { path: 'about', element: <About /> },
      { path: 'user/:id', element: <UserDetail /> }
    ]
  }
])

export default router
```

```tsx
// 顶层用一个 Suspense 兜住所有懒加载页面（也可按布局分别包）
// main.tsx
<Suspense fallback={<PageLoading />}>
  <RouterProvider router={router} />
</Suspense>
```

数据路由（v6.4+）也支持路由级 `lazy`，实现"加载组件 + loader"一起按需：

```tsx
{
  path: 'list',
  lazy: async () => {
    const { ListPage } = await import('@/pages/ListPage')   // 需具名导出
    return { Component: ListPage }
  }
}
```

# 九、状态管理

## 9.1 为什么需要全局状态

组件树内部通信用 props/Context 即可。当状态需要在**大量互不相关的组件**间共享、或模块逻辑复杂时，引入全局状态库收益明显：

- 单一数据源 + 可预测的更新方式；
- 方便调试（时间旅行、DevTools 查看）；
- 支持中间件（日志、持久化、异步）；
- 与组件树解耦，逻辑可独立测试。

**选型建议**：

| 方案 | 定位 | 推荐场景 |
| --- | --- | --- |
| 组件内 `useState` | 本地状态 | 单组件 |
| 状态提升 | 共享于局部树 | 少量相邻组件 |
| `Context` | 跨层共享 | 低频"配置类"（主题/语言），无需复杂逻辑 |
| `useReducer` + Context | 中量级 | 一个模块内较复杂的状态 |
| Redux Toolkit | 大型/团队规范 | 中大型应用、强类型、需要完善工具链 |
| Zustand | 轻量 | 中小型应用，追求简洁（官方推荐之一） |

## 9.2 Redux Toolkit（官方推荐）

Redux Toolkit（RTK）是 Redux 的官方现代写法，内置 immer（可直接改 state）与 thunk：

```
npm install @reduxjs/toolkit react-redux
```

**定义 slice（对标 Pinia 的 store 模块）**：

```ts
// src/store/counterSlice.ts
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

interface CounterState { value: number }

const initialState: CounterState = { value: 0 }

const counterSlice = createSlice({
  name: 'counter',
  initialState,
  reducers: {
    // immer 加持：看起来是直接修改，实际生成新状态
    increment(state, action: PayloadAction<number>) {
      state.value += action.payload
    },
    reset(state) {
      state.value = 0
    }
  }
})

export const { increment, reset } = counterSlice.actions
export default counterSlice.reducer
```

**配置 store**：

```ts
// src/store/index.ts
import { configureStore } from '@reduxjs/toolkit'
import counter from './counterSlice'
import user from './userSlice'

export const store = configureStore({
  reducer: { counter, user }
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
```

**类型化 Hooks**：

```ts
// src/store/hooks.ts
import { useDispatch, useSelector } from 'react-redux'
import type { RootState, AppDispatch } from './index'

export const useAppDispatch = () => useDispatch<AppDispatch>()
export const useAppSelector = useSelector.withTypes<RootState>()
```

**异步 action（createAsyncThunk）**：

```ts
// src/store/userSlice.ts
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'

interface UserState {
  roles: string[]
  status: 'idle' | 'loading' | 'succeeded' | 'failed'
}

const initialState: UserState = { roles: [], status: 'idle' }

// 异步 thunk：自动生成 pending / fulfilled / rejected 三种 action
export const fetchRoles = createAsyncThunk('user/fetchRoles', async () => {
  const res = await fetch('/api/roles')
  return (await res.json()) as string[]
})

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchRoles.pending, (state) => { state.status = 'loading' })
      .addCase(fetchRoles.fulfilled, (state, action) => {
        state.status = 'succeeded'
        state.roles = action.payload
      })
      .addCase(fetchRoles.rejected, (state) => { state.status = 'failed' })
  }
})

export default userSlice.reducer
```

**在组件中使用**：

```tsx
// main.tsx 先注入 Provider
import { Provider } from 'react-redux'
import { store } from '@/store'

createRoot(document.getElementById('root')!).render(
  <Provider store={store}>
    <App />
  </Provider>
)
```

```tsx
// 组件内读取与派发
import { useAppDispatch, useAppSelector } from '@/store/hooks'
import { increment, reset } from '@/store/counterSlice'
import { fetchRoles } from '@/store/userSlice'

function Counter() {
  const count = useAppSelector((s) => s.counter.value)
  const roles = useAppSelector((s) => s.user.roles)
  const dispatch = useAppDispatch()

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => dispatch(increment(1))}>+1</button>
      <button onClick={() => dispatch(reset())}>重置</button>
      <button onClick={() => dispatch(fetchRoles())}>加载角色</button>
    </div>
  )
}
```

要点：

- 用 `useAppSelector` 尽量**选取最小粒度**（返回原始值，避免内联新建对象导致反复重渲染）；
- RTK 的 state 不可直接在外面修改，一律走 action；
- 配合 Redux DevTools 可回放、跳转状态。

## 9.3 Zustand（轻量方案）

Zustand 以 API 极简著称，不需要 Provider 包裹，可脱离组件使用：

```
npm install zustand
```

```ts
// src/store/counter.ts
import { create } from 'zustand'

interface CounterStore {
  count: number
  increment: (step?: number) => void
  reset: () => void
}

export const useCounter = create<CounterStore>((set) => ({
  count: 0,
  increment: (step = 1) => set((s) => ({ count: s.count + step })),
  reset: () => set({ count: 0 })
}))
```

```tsx
// 组件中使用
import { useCounter } from '@/store/counter'

function Counter() {
  // 选最小粒度订阅，避免多余渲染
  const count = useCounter((s) => s.count)
  const increment = useCounter((s) => s.increment)

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => increment()}>+1</button>
    </div>
  )
}
```

异步与持久化：

```ts
// src/store/user.ts
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface UserStore {
  token: string
  login: (token: string) => void
  logout: () => void
}

export const useUser = create<UserStore>()(
  persist(
    (set) => ({
      token: '',
      login: (token) => set({ token }),
      logout: () => set({ token: '' })
    }),
    {
      name: 'user-store',          // localStorage key
      partialize: (s) => ({ token: s.token })   // 只持久化部分字段
    }
  )
)
```

## 9.4 小结：在组件中状态访问对照

| 方案 | 读取 | 更新 | 备注 |
| --- | --- | --- | --- |
| Context | `useContext(Ctx)` | Provider 下发函数 | 高频变更需防全量重渲染 |
| Redux Toolkit | `useAppSelector` | `dispatch(action)` | 需 Provider；middleware 丰富 |
| Zustand | `useStore(s => s.x)` | store 内 action / `set` | 免 Provider；可脱离组件 |
| useReducer | `state` | `dispatch(action)` | 局部使用 |

# 十、进阶特性

## 10.1 React.memo：组件缓存

父组件重渲染默认会让**整棵子树**都重渲染。`memo` 对组件做浅比较：props 引用都没变时跳过重渲染（对标 `v-memo` / 手动 shouldComponentUpdate）：

```tsx
import { memo, useState, useCallback } from 'react'

// 用 memo 包裹：父组件重渲染时，若 props 浅比较相等则跳过
const Child = memo(function Child({ label, count, onIncr }: {
  label: string
  count: number
  onIncr: () => void
}) {
  console.log('Child 渲染:', label)
  return <button onClick={onIncr}>{label}: {count}</button>
})

function Parent() {
  const [a, setA] = useState(0)
  const [b, setB] = useState(0)

  // 用 useCallback 稳定函数引用，否则每次新建，memo 浅比较失效
  const incrA = useCallback(() => setA(a => a + 1), [])

  return (
    <div>
      {/* 修改 b 时，Child(a) 因 props 未变而跳过渲染 */}
      <Child label="A" count={a} onIncr={incrA} />
      <Child label="B" count={b} onIncr={() => setB(b => b + 1)} />
      <button onClick={() => setB(b => b + 1)}>改 B</button>
    </div>
  )
}
```

**优化金字塔（按需使用，先测再优化）**：

1. 默认 React 渲染已经够快，能用"状态下沉 / 拆分组件"解决就别用 memo；
2. `memo` 适合"props 引用稳定、子树渲染昂贵"的组件；
3. 配合 `useMemo` / `useCallback` 保持 props 引用稳定。

## 10.2 Error Boundary 错误边界

渲染过程中抛错会让整个应用白屏。错误边界（Error Boundary）可以捕获**子树**的渲染错误并展示降级 UI。目前它只能用**类组件**实现：

```tsx
// components/ErrorBoundary.tsx
import { Component } from 'react'
import type { ErrorInfo, ReactNode } from 'react'

interface Props { children: ReactNode; fallback?: ReactNode }
interface State { hasError: boolean; message: string }

class ErrorBoundary extends Component<Props, State> {
  state: State = { hasError: false, message: '' }

  // 渲染阶段捕获错误：返回新 state 触发降级 UI
  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, message: error.message }
  }

  // 提交阶段捕获：用于上报日志
  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('页面错误:', error, info.componentStack)
    // 上报监控平台：report(error, info)
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback ?? <h3>页面出错了：{this.state.message}</h3>
    }
    return this.props.children
  }
}

export default ErrorBoundary
```

```tsx
// 使用：用边界包裹可能出现错误的区域
<ErrorBoundary>
  <UserProfile />
</ErrorBoundary>
```

注意：错误边界只能捕获渲染、生命周期、构造函数里的错误；**捕获不到**事件处理器、异步代码（setTimeout/Promise）里的错误——那些错误请用 try/catch 处理。React 19 仍在探索函数组件版本的错误边界 API。

## 10.3 Suspense 与 React.lazy：异步组件与加载态

`React.lazy` 定义异步组件（代码分割），`<Suspense>` 在加载完成前显示回退内容（对标 `defineAsyncComponent` + `<Suspense>`）：

```tsx
import { lazy, Suspense } from 'react'

// 常规异步组件（等价 () => import(...)）
const HeavyChart = lazy(() => import('@/components/HeavyChart'))

function Dashboard() {
  return (
    <Suspense
      fallback={<div className="loading">图表加载中…</div>}
    >
      <HeavyChart />
    </Suspense>
  )
}
```

要点：

- `lazy` 的模块需**默认导出**组件；
- 多个异步组件可共用一个 `<Suspense>`（任一未完成都显示 fallback），也可各自分包独立 loading；
- Suspense 目前主要用于懒加载与数据框架（Next.js / TanStack Router）；一般业务数据请求仍用 `useEffect` + loading state。

## 10.4 createPortal：传送门

把子内容渲染到组件树以外的 DOM 节点（通常是 `document.body`），常用于弹窗、抽屉、下拉。对标 Vue 的 `<Teleport>`：

```tsx
// components/Modal.tsx
import { createPortal } from 'react-dom'

function Modal({ open, title, onClose, children }: {
  open: boolean
  title: string
  onClose: () => void
  children: React.ReactNode
}) {
  if (!open) return null

  // 渲染到 body 下，避免被父级 overflow/transform/z-index 影响
  return createPortal(
    <div className="modal-mask" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-title">
          {title}
          <button onClick={onClose}>×</button>
        </div>
        <div className="modal-body">{children}</div>
      </div>
    </div>,
    document.body
  )
}

export default Modal
```

```tsx
// 使用
function Demo() {
  const [open, setOpen] = useState(false)
  return (
    <div>
      <button onClick={() => setOpen(true)}>打开弹窗</button>
      <Modal open={open} title="提示" onClose={() => setOpen(false)}>
        <p>这是通过 createPortal 渲染的弹窗</p>
      </Modal>
    </div>
  )
}
```

重要特性：即使 DOM 位置在 body，**事件仍然遵循 React 组件树的冒泡**，样式继承（如 CSS 变量）则失效，需在弹窗内自行定义。

## 10.5 高阶组件 HOC 与 Render Props（历史方案）

Hooks 出现前，跨组件复用逻辑主要靠 HOC（Higher-Order Component）与 Render Props。现在**新代码优先自定义 Hook**，但存量代码（尤其老 antd / 三方库）仍会遇到，需能读懂：

```tsx
// HOC：接收组件，返回增强后的组件
function withLogging<P extends object>(Wrapped: React.ComponentType<P>) {
  return function Logged(props: P) {
    console.log('渲染 <Logged>', props)
    return <Wrapped {...props} />
  }
}

// 用法：const SafeButton = withLogging(Button)
```

```tsx
// Render Props：把"要渲染什么"作为函数 prop 传下去
function MouseTracker({ render }: { render: (pos: { x: number; y: number }) => React.ReactNode }) {
  const [pos, setPos] = useState({ x: 0, y: 0 })
  // ...监听 mousemove
  return <>{render(pos)}</>
}

// 用法：<MouseTracker render={({ x, y }) => <span>{x},{y}</span>} />
```

对比结论：同样的逻辑用自定义 Hook 更简洁、无嵌套、类型更好（见 5.5 的 `useMouse`），所以 HOC / Render Props 属于"了解并能改造"的旧时代方案。

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

配合自定义 Hook 在组件中使用：

```tsx
// src/hooks/useAsyncData.ts（复用 5.5 版本，接收 fetcher 并支持重试）
const { data, loading, error, reload } = useAsyncData(() => getUser(id))
```

## 11.2 Ant Design 集成

```
npm install antd @ant-design/icons
```

Antd v5 采用 CSS-in-JS，无需单独引入样式文件，支持按需（tree-shaking 自动）+ 主题定制：

```tsx
// src/main.tsx：全局 ConfigProvider 定制主题语言
import { ConfigProvider, App as AntApp } from 'antd'
import zhCN from 'antd/locale/zh_CN'

createRoot(document.getElementById('root')!).render(
  <ConfigProvider
    locale={zhCN}
    theme={{
      token: {
        colorPrimary: '#4db6ac',     // 主题色
        borderRadius: 6
      }
    }}
  >
    <AntApp>   {/* 提供 message/notification/modal 的上下文 */}
      <App />
    </AntApp>
  </ConfigProvider>
)
```

```tsx
// 页面中使用
import { useState } from 'react'
import { Button, Modal, App, Table } from 'antd'
import type { ColumnsType } from 'antd/es/table'

interface UserRow { id: number; name: string; age: number }

function UserPage() {
  const { message } = App.useApp()          // 上下文里的 message
  const [open, setOpen] = useState(false)
  const [data, setData] = useState<UserRow[]>([])

  const columns: ColumnsType<UserRow> = [
    { title: 'ID', dataIndex: 'id' },
    { title: '姓名', dataIndex: 'name' },
    {
      title: '操作',
      render: (_, row) => (
        <Button type="link" onClick={() => message.success(`编辑 ${row.name}`)}>
          编辑
        </Button>
      )
    }
  ]

  return (
    <div>
      <Button type="primary" onClick={() => setOpen(true)}>新建</Button>
      <Table rowKey="id" columns={columns} dataSource={data} />

      <Modal open={open} title="新建用户" onCancel={() => setOpen(false)} onOk={() => {
        message.success('保存成功')
        setOpen(false)
      }}>
        <p>表单内容</p>
      </Modal>
    </div>
  )
}
```

要点：

- 交互式反馈（`message` / `notification` / `Modal.confirm`）需在 `<AntApp>` 内使用，且用 `App.useApp()` 获取，避免静态调用拿不到主题/上下文；
- Table 记得设置 `rowKey`（等价于列表渲染的 key）；
- 中后台常用"ProComponents"（`@ant-design/pro-components`）可进一步减少表单/表格样板代码。

## 11.3 常用自定义 Hooks 清单

| Hook | 说明 | 可参考 |
| --- | --- | --- |
| `useMouse()` | 鼠标坐标 | 5.5 |
| `useDebounceValue(src, delay)` | 防抖值 | 5.5 |
| `useThrottleValue` | 节流值 | 自行封装 |
| `useLocalStorage(key, init)` | localStorage 响应式读写 | 自行封装 |
| `useEventListener(el, event, handler)` | 自动绑定/解绑事件 | 社区 |
| `useAsyncData(fetcher)` | 请求，含 loading/error/重试 | 5.5 / 11.1 |
| `useIntersectionObserver` | 懒加载、进入视口检测 | 社区 |
| `useToggle` / `useBoolean` | 布尔开关 | 社区 |
| `useCountdown` | 倒计时 | 社区 |

> 生产项目可优先引入成熟库 `ahooks`（蚂蚁开源）或 `usehooks-ts`，避免重复造轮子。

## 11.4 性能优化实践

1. **代码分割**：路由/大组件全部用 `React.lazy` + `Suspense`（见 8.6）。
2. **状态下沉 / 拆分组件**：把变化频繁的 state 放到更小的组件里，减小重渲染范围。
3. **memo + useMemo/useCallback**：仅对渲染昂贵的子树、且 props 引用稳定的组件使用（见 10.1），先测量再优化。
4. **useTransition / useDeferredValue**：大数据过滤、列表更新等"可延迟"的工作降级处理（见 5.6）。
5. **虚拟列表**：超长列表用 `react-window` / `@tanstack/react-virtual` 只渲染可视区。
6. **图片懒加载**：原生 `loading="lazy"`，配合 `srcset`/占位图。
7. **UI 库按需 + 主题 token**：antd v5 自动 tree-shaking 与按需样式；避免全量静态引入样式。
8. **分包优化**：`build.rollupOptions.output.manualChunks` 拆分 react-vendor 与业务大依赖（见 7.1）。
9. **避免大对象塞 Context**：高频更新放 state 库并订阅最小切片，避免整树消费组件重渲染。
10. **key 稳定**：列表 key 用唯一 id，避免用 index（见 3.4）。
11. **请求层去重缓存**：React Query / SWR 可自动缓存、去重、后台刷新，减少重复请求。
12. **渲染时不做重活**：不要在组件函数体内直接执行昂贵计算或副作用，放到 useMemo / useEffect。

## 11.5 常见坑与 FAQ

| 问题 | 原因与解决 |
| --- | --- |
| 改了数组/对象但视图不更新 | 原地修改了引用。用展开/map/filter 生成新引用（见 4.3） |
| `setCount(count + 1)` 连点只 +1 | 同一渲染内基于旧值。用函数式更新 `setCount(c => c + 1)` |
| `useEffect` 无限循环 / 疯狂请求 | 依赖数组里有"每次新建的对象/函数"，或 effect 内同步改自身依赖的 state。用 useMemo/useCallback 稳定引用，把 setState 移到事件/回调里 |
| 列表 key 用 index 内容错乱 | 增删/排序时复用错乱。改用稳定唯一 id |
| `{count && ...}` 渲染出 0 | `0` 是合法渲染值。用 `count > 0 && ...` |
| 想改 props 里的值 | props 只读。状态提升到父组件，用回调或全局状态 |
| 在渲染函数里发请求 | 渲染会重复执行，请求应放 useEffect / 事件处理 |
| StrictMode 下 effect 执行两次 | 开发模式故意为之，用于暴露副作用问题，属预期行为 |
| 卸载后 setState 警告 | effect 里异步回调在卸载后仍 setState。用 cleanup 取消订阅/置 cancelled 标记 |
| Context 一改，消费组件全重渲染 | value 每次渲染新建导致。用 useMemo 缓存 value、拆细 Context |
| 在组件函数内定义子组件导致状态重置 | 每次渲染都新建组件类型。把子组件定义到顶层 |
| 事件回调里拿到的 state 是旧值 | 闭包快照。需要最新值用 ref 或函数式更新（见 4.2） |
| useEffect 依赖漏写变量 | ESLint exhaustive-deps 会告警，补全依赖或重构逻辑 |
| 引入组件忘了默认导出 | lazy 加载要求默认导出组件 |

## 11.6 目录结构建议（中大型项目）

```
src/
├── api/                 # 接口层（按模块拆分）
├── assets/              # 静态资源
├── components/          # 通用组件（按需按模块分子目录）
├── hooks/               # 自定义 Hooks
├── layouts/             # 布局组件
├── pages/               # 页面级组件（按路由组织）
├── router/              # 路由配置
├── store/               # 全局状态（Zustand / Redux）
├── context/             # Context 定义与 Provider
├── styles/              # 全局样式（variables/mixins）
├── types/               # 全局类型定义
├── utils/               # 工具函数
├── App.tsx
└── main.tsx
```

# 十二、附录

## 12.1 官方资源

- React 官方文档：https://react.dev/ （中文：https://zh-hans.react.dev/ ）
- React 19 发布说明：https://react.dev/blog/2024/12/05/react-19
- React Router：https://reactrouter.com/
- Redux Toolkit：https://redux-toolkit.js.org/
- Zustand：https://zustand-demo.pmnd.rs/ （文档：https://zustand.docs.pmnd.rs/）
- Next.js：https://nextjs.org/
- Vite：https://cn.vitejs.dev/
- Ant Design：https://ant.design/
- ahooks（Hooks 工具库）：https://ahooks.js.org/

## 12.2 术语速查

| 术语 | 含义 |
| --- | --- |
| JSX | JavaScript 语法扩展，`React.createElement` 的语法糖 |
| Component | 组件，返回 JSX 的（纯）函数 |
| Props | 父传子的只读数据 |
| State | 组件内部状态，变化触发重渲染 |
| Fiber | React 渲染工作的调度单元/数据结构 |
| Reconciler / Diff | 对比新旧树、计算最小 DOM 更新的过程 |
| Hooks | 函数组件中挂载状态与副作用的一组 API |
| 受控组件 | value 由 state 管理、onChange 回写的表单元素 |
| 非受控组件 | 值存于 DOM，通过 ref 读取 |
| 状态提升 | 把共享 state 上移到共同父组件 |
| Context | 跨层级共享数据的机制 |
| Suspense | 让组件"等待"并显示回退 UI 的机制 |
| Error Boundary | 捕获子树渲染错误并降级 UI 的组件 |
| Portal | 把子树渲染到组件树外的 DOM（createPortal） |
| SyntheticEvent | React 合成事件，跨浏览器封装并委托分发 |
| memo | 对组件做 props 浅比较、跳过重渲染的优化 |
| Concurrent | React 18+ 的并发渲染能力（可中断渲染） |
| StrictMode | 开发期严格检查，双调用以暴露副作用问题 |
| 单向数据流 | 数据自上而下、修改点收敛的架构约束 |

> 小贴士：这套"React 教程"与同目录的「Vue3 教程」为姊妹篇，两者逐章对照（响应式/State、Composable/Hook、slot/children、provide-inject/Context、router-link/Link、Pinia/RTK-Zustand 等），适合作为前端框架知识的整体收尾笔记。
