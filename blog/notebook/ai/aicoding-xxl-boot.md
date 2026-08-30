# AI 范式越迁：使用 XXL-BOOT SKILL 实现一句话直生业务

> 从「一行 SQL 生成代码」到「一句需求直生业务」——业务开发正式进入 AI 范式新时代。

随着大模型编程助手的成熟，业务交付范式正在被重塑。XXL-BOOT v2.1.0 内置 `xxl-boot-monolith / xxl-boot-vue / xxl-boot-react` 三大开发 SKILL，让 AI 编程助手（如 opencode、codex）化身「资深平台工程师」：
**你只需描述业务诉求，AI 即按平台规范完成「需求落盘 → 澄清 → 建表 → 后端 → 前端 → 菜单权限 → 联调验证」全流程，业务代码零手写。**

---

## 一、什么是 XXL-BOOT SKILL

XXL-BOOT 以 Monorepo 一套仓库托管三种运行模式，因此为每种模式各内置一个开发 SKILL，存放于仓库 `.agents/skills/` 目录：

```
xxl-boot/
└── .agents/skills/
    ├── xxl-boot-monolith/   # 单体模式：xxl-boot-admin（8080，FreeMarker + AdminLTE）
    ├── xxl-boot-vue/        # Vue 分离模式：xxl-boot-api + xxl-boot-ui-vue（8090 + 3000）
    └── xxl-boot-react/      # React 分离模式：xxl-boot-api + xxl-boot-ui-react（8090 + 4000）
```

每个 SKILL 都是 AI 的「项目内专业规范」，封装了平台多年沉淀的工程约束：

- **工程结构速览**：后端 `business/{module}`、前端 `views|api|types|pages…` 等落位路径；
- **后端落位清单**：实体 / Mapper / Service / Controller 件套、包路径、方法顺序、分页与校验约定；
- **前端落位清单**：Vue 三文件（types/api/view）、React 三文件（types/services/pages）及列表页骨架；
- **菜单权限 SQL 模板**：资源菜单 + 按钮 + 角色授权一键生成；
- **校验清单**：交付前自动自检，保证 AI 产物与人工/生成器产物完全等价。

> AI 编程助手打开 XXL-BOOT 仓库后，会依据任务自动识别并加载匹配的 SKILL，无需人工干预。

---

## 二、零编码业务交付全流程

AI 按 SKILL「标准流程」作业，覆盖从需求到上线验证的完整闭环：

```
需求落盘 → 澄清 → 建表 → 生成代码 → 落位 → 菜单权限 → 联调验证 → 人工复核
```

### 第 0 步：需求落盘（AI 先建立）

AI 在本目录下为每个需求自动创建专属子目录 `xxl-boot-spec/{yyyyMMdd}-{business}/`，将需求结论、方案（`plan.md`）、SQL 全部沉淀于此，形成可追溯、可复用的需求档案：

```
xxl-boot-spec/
└── 20260830-product/
    ├── plan.md            # 六块方案：需求/数据库/菜单授权/后端/前端/验证
    ├── product-table.sql  # 建表 SQL
    └── product-init.sql   # 菜单 + 按钮 + 授权 SQL
```

### 第 1 步：需求澄清（AI 不写代码前先问）

SKILL 强制 AI 动手前先做需求澄清，就以下要点逐项确认你的诉求：

- 模块与业务命名（`{module}/{business}`）及目录归属；
- 核心字段、状态/枚举下拉、是否需文件上传/富文本等特殊组件；
- 页面形态（标准 CRUD / 详情页 / 多页签）；
- 菜单 + 按钮 + 角色授权是否一并处理；
- 出码方式（AI 按模板直生 或 后台「代码生成」）；
- 验证范围与启动端口。

全部确认后，AI 汇总形成数据模型与开发方案，回填 `plan.md`。

### 第 2 步：建表

AI 按平台规范自动产出 `xxl_boot_*` 建表 SQL：公共字段 `id / add_time / update_time`、状态字段 `TINYINT`、唯一索引 `i_` 前缀、字段一律 `COMMENT` 注释，脚本落盘于需求子目录。

### 第 3 步：生成代码并按模板直生落位

SKILL 缺省策略为 AI 直接读内置模板渲染等价代码并自动落位，产出与后台生成器完全一致：

- **后端**：`business/{module}/` 下 6 件套（Controller / Service / ServiceImpl / Mapper / Mapper.xml / Entity），Mapper XML 落 `resources/mapper/{module}/`；
- **前端 Vue**：`views|api|types/{module}/{page}` 三文件，并在 `types/api.ts` barrel 登记一行；
- **前端 React**：`pages|services|types/{module}/{page}` 三文件；
- **单体**：实体等 6 件套 + FreeMarker 页面落 `templates/business/{module}/`。

### 第 4 步：菜单 / 权限注册

AI 自动生成菜单初始化 SQL，插入资源菜单（type=1）+ 按钮（type=2）+ 角色授权（role_id=1）：

```sql
INSERT INTO `xxl_boot_resource` (...) VALUES (0, '产品管理', 1, 'product:product', '/product/product', '', 999, 0, 0, now(), now());
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `xxl_boot_resource` (...) VALUES
    (@parentId, '产品新增', 2, 'product:product:add', '', '', 1, 0, 0, now(), now()),
    (@parentId, '产品修改', 2, 'product:product:edit', '', '', 2, 0, 0, now(), now()),
    (@parentId, '产品删除', 2, 'product:product:remove', '', '', 3, 0, 0, now(), now());
INSERT INTO `xxl_boot_role_res` (`role_id`,`res_id`,`add_time`,`update_time`)
VALUES (1, @parentId, now(), now()), (1, @parentId+1, ...), (1, @parentId+2, ...), (1, @parentId+3, ...);
```

> 菜单由数据库 `xxl_boot_resource` 驱动、`url` 同时充当路由 path 与前端组件定位 key，**全程零路由改动**。

### 第 5 步：联调验证 + 人工复核

AI 自动启动服务（api 8090 + 前端 3000/4000，或 admin 8080）完成本地联调，按 SKILL「校验清单」自检：菜单可见、CRUD/搜索可用、无权限按钮隐藏、空参数友好提示。最后交由人工复核后合并 PR，即完成上线交付。

---

## 三、实操演示：一句话开发「产品信息管理」

以 opencode + `xxl-boot-vue` SKILL 为例，演示完整交付过程：

### 第 1 步：描述需求

用 AI 助手打开 XXL-BOOT 仓库，以 `/xxl-boot-vue` 前缀调用 Skill 并描述诉求：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_1.png" width="900"/></p>

> 需求示例：“`/xxl-boot-vue` 新增一个功能模块，管理产品信息，维护 产品名称、介绍、生产时间 等。”

### 第 2 步：需求澄清与确认

AI 就模块命名、核心字段、页面形态、菜单按钮权限等连续提问并确认细节：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_2.png" width="900"/></p>

确认完毕后，AI 汇总需求细节，形成数据模型与开发方案：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_3.png" width="800"/></p>

### 第 3 步：开发编排与自动执行

AI 输出开发 TODO 列表，依次执行「代码现状分析 → 业务模型与菜单/授权 SQL → 后端 7 件套 + 状态枚举 → 前端 3 文件 + barrel → 前后端工程编辑 → 启动 api 与前端，脚本本地联调」：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_4.png" width="900"/></p>

### 第 4 步：交付与验证

开发完成，AI 交付 SQL、前端、后端全套产物与验证摘要：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_5.png" width="900"/></p>

### 第 5 步：联调验收

AI 自动启动 api(8090) 与前端(3000) 联调后，人工刷新浏览器，从菜单进入「产品管理」页面，即可开展查询与增删改操作：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_6.png" width="900"/></p>

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_7.png" width="900"/></p>

### 第 6 步：代码合并

AI 生成的代码经 review 确认后，形成代码 PR 提交到仓库，交付完成：

<p align="center"><img src="https://www.xuxueli.com/project/static/xxl-boot/images/skill/img_skill_8.png" width="400"/></p>

---

## 四、AI 范式 vs 传统范式

| 对比项 | A、传统手工开发         | B、内置代码生成器                   | C、AI + SKILL 驱动            |
|---|-------------------------|-------------------------------------|-------------------------------|
| 交付方式 | 逐行手写全套后端 + 前端 | 后台录入建表 SQL 一键出码           | 一句话描述需求，AI 直生并落位 |
| 产出 | 符合规范的代码          | 后端 6 件套 + 前端三文件 + 菜单 SQL | 与生成器完全等价并附自动落位  |
| 登录后台 / 复制粘贴 | 不需要                  | 需要                                | 不需要，AI 直接写盘           |
| 自检 | 人工 review             | 无                                  | SKILL 校验清单自动自检        |
| 效率 | 代码量大、周期长        | 下单即用、稳定出码                  | 全自动编排，极致效率          |
| 三者关系 | 通用兜底                | 界面化出码                          | 对话式出码                    |

> 三种方式共用同一套 SKILL 规范，落位路径与代码规范完全一致，可自由切换、混合使用。

---

## 五、为什么能做到「零编码却零偏差」

AI 不是「自由发挥」，而是严格遵循平台规范：

- **规范即模板**：直接渲染内置 `templates/tool/codegen/{java,vue3,react}/*.ftl` / `codegen-module/*.ftl` 等价骨架，与后台生成器出码天然一致；
- **落位即约定**：SKILL 固化后端 `business/{module}`、前端 `views|api|types/{module}/{page}` 等落位路径，AI 按清单逐文件落位；
- **验收即门槛**：每个 SKILL 内置「校验清单」（编译通过 / `@XxlSso` / 方法顺序 / `NOW()` / 权限 / 联调…），AI 交付前必须逐项自检通过；
- **需求即档案**：`xxl-boot-spec/{yyyyMMdd}-{business}/` 方案 + SQL 全程落盘，可追溯、可复用。

---

## 六、立即开启 AI 范式交付

1. **打开仓库**：使用 AI 编程助手（如 opencode）打开 XXL-BOOT 仓库；
2. **调用 Skill**：按运行模式使用 `/xxl-boot-vue`、`/xxl-boot-react` 或 `/xxl-boot-monolith`；
3. **描述诉求**：一句话说明要开发的业务（AI 会主动澄清细节）；
4. **验收交付**：AI 自动落位、联调、自检，人工复核后合并 PR 即可上线。

XXL-BOOT 已经为 AI 范式准备好了「规范、模板、落位、验收」的完整闭环——**零编码，从这句话开始**。

---

- 快速开始：<https://www.xuxueli.com/xxl-boot/>
- 源码仓库：<https://github.com/xuxueli/xxl-boot>
