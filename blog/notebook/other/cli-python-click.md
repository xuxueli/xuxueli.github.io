<h2 style="color:#4db6ac !important" >CLI开发指南：使用Python Click构建命令行工具</h2>
> 【原创】2016/06/15

[TOCM]

[TOC]

## 一、CLI 说明

在现代软件开发中，命令行界面（CLI）仍然是许多开发者和系统管理员不可或缺的工具。Python 提供了强大的 `click` 库，用于快速构建功能强大、易于使用的命令行应用程序。本指南将带你从零开始，使用 `click` 构建一个结构清晰、功能完整的 CLI 工具。

## 二、Click 介绍

Click 是一个 Python 包，用于以可组合的方式创建漂亮的命令行界面。它具有高度可配置性，并支持命令嵌套、参数处理、选项解析等功能。Click 的设计哲学是"命令行界面应该像函数一样简单"，因此它非常适合构建结构化的 CLI 应用。

### 2.1、安装 Click

在开始之前，请确保你已经安装了 Click：

```bash
## 或者 pip3
pip install click
```

### 2.2、创建第一个命令

Click 的核心是通过装饰器来定义命令。以下是一个简单的示例：

```python
import click

@click.command()
def hello():
    """打印一条问候语"""
    click.echo('Hello, World!')

if __name__ == '__main__':
    hello()
```

在这个例子中：
- `@click.command()` 将函数 `hello` 转换为一个命令。
- `click.echo()` 是 Click 推荐的输出方式，它比 `print()` 更加兼容。
- 文档字符串 `"""打印一条问候语"""` 会自动成为命令的帮助信息。

### 2.3、添加参数与选项

Click 支持两种主要的输入方式：参数（Arguments）和选项（Options）。

#### 2.3.1. 参数（Arguments）

参数是位置相关的输入，例如：

```python
@click.command()
@click.argument('name')
def greet(name):
    """根据名字打印问候语"""
    click.echo(f'Hello, {name}!')

if __name__ == '__main__':
    greet()
```

运行命令：

```bash
python greet.py Alice
# 输出: Hello, Alice!
```

#### 2.3.2. 选项（Options）

选项是带标志的输入，例如：

```python
@click.command()
@click.option('--count', default=1, help='打印次数')
@click.option('--name', prompt='请输入你的名字', help='用户的名字')
def greet(count, name):
    """根据名字和次数打印问候语"""
    for _ in range(count):
        click.echo(f'Hello, {name}!')

if __name__ == '__main__':
    greet()
```

运行命令：

```bash
python greet.py --count 3 --name Alice
# 输出:
# Hello, Alice!
# Hello, Alice!
# Hello, Alice!
```

### 2.4、构建多命令 CLI

Click 支持通过组（Group）来组织多个子命令，这非常适合构建功能丰富的 CLI 工具。

```python
import click

@click.group()
def cli():
    """一个简单的 CLI 工具"""
    pass

@cli.command()
def init():
    """初始化项目"""
    click.echo('项目初始化完成')

@cli.command()
def run():
    """运行项目"""
    click.echo('项目正在运行...')

if __name__ == '__main__':
    cli()
```

运行命令：

```bash
python cli.py init
# 输出: 项目初始化完成

python cli.py run
# 输出: 项目正在运行...
```

### 2.5、高级功能

#### 2.5.1. 参数验证

Click 提供了多种参数类型和验证方式，例如：

```python
@click.command()
@click.option('--age', type=int, prompt='请输入年龄')
def check_age(age):
    if age >= 18:
        click.echo('你已成年')
    else:
        click.echo('你还未成年')
```

#### 2.5.2. 文件处理

Click 支持文件类型的参数，可以自动处理文件的打开与关闭：

```python
@click.command()
@click.argument('input_file', type=click.File('r'))
def process_file(input_file):
    content = input_file.read()
    click.echo(f'文件内容: {content}')
```

#### 2.5.3. 环境变量支持

Click 可以从环境变量中读取默认值：

```python
@click.command()
@click.option('--name', envvar='USER_NAME', help='用户名称')
def greet(name):
    click.echo(f'Hello, {name}!')
```

### 2.6、资料

Click 是一个强大而灵活的 Python 库，能够帮助你快速构建功能丰富的命令行工具。
更多可参考文档：
- [官方文档](https://click.palletsprojects.com/en/8.1.x/)
- [中文文档](https://click-docs-zh-cn.readthedocs.io/zh/latest/)
- [GitHub](https://github.com/pallets/click)

## 三、Calculator CLI 示例

一个简单而强大的命令行计算器 CLI 工具，支持基本的算术运算。

### 3.1、功能介绍

- ✅ **加法运算** - 两个数相加
- ✅ **减法运算** - 两个数相减
- ✅ **乘法运算** - 两个数相乘
- ✅ **除法运算** - 两个数相除（包含除零保护）
- ✅ **友好的命令行界面** - 基于 Click 框架
- ✅ **版本管理** - 支持查看版本信息
- ✅ **帮助文档** - 完整的命令帮助信息
- ✅ **错误处理** - 友好的错误提示

### 3.2、系统要求

- Python 3.6 或更高版本
- pip (Python 包管理器)
- macOS / Linux / Windows 操作系统

### 3.3、安装指南

#### 3.3.1、方法一：使用安装脚本（推荐）

```bash
# 进入项目目录
cd /path/to/cli

# 赋予执行权限
chmod +x install.sh

# 运行安装脚本
./install.sh
```

#### 3.3.2、方法二：手动安装

```bash
# 进入项目目录
cd /path/to/cli

# 安装依赖
pip3 install click>=8.0.0

# 安装包
pip3 install -e .
```

#### 3.3.3、验证安装

```bash
# 查看版本
calc --version

# 查看帮助
calc --help
```

### 3.4、卸载指南

#### 3.4.1、方法一：使用卸载脚本（推荐）

```bash
# 进入项目目录
cd /path/to/cli

# 赋予执行权限
chmod +x uninstall.sh

# 运行卸载脚本
./uninstall.sh
```

#### 3.4.2、方法二：手动卸载

```bash
# 卸载包
pip3 uninstall calculator-cli

# 清理缓存文件（可选）
find /path/to/cli -type d -name "__pycache__" -exec rm -rf {} + 2>/dev/null || true
rm -rf build dist *.egg-info
```

### 3.5、使用方法

#### 3.5.1、基本语法

```bash
calc <command> <number1> <number2>
```

#### 3.5.2、可用命令

| 命令 | 说明 | 示例 |
|------|------|------|
| `add` | 加法运算 | `calc add 5 3` |
| `subtract` | 减法运算 | `calc subtract 10 4` |
| `multiply` | 乘法运算 | `calc multiply 6 7` |
| `divide` | 除法运算 | `calc divide 20 4` |

#### 3.5.3、全局选项

| 选项 | 说明 |
|------|------|
| `--help` | 显示帮助信息 |
| `--version` | 显示版本信息 |

### 3.6、命令示例

#### 3.6.1. 加法运算

```bash
$ calc add 5 3
Result: 5.0 + 3.0 = 8.0
```

#### 3.6.2. 减法运算

```bash
$ calc subtract 10 4
Result: 10.0 - 4.0 = 6.0
```

#### 3.6.3. 乘法运算

```bash
$ calc multiply 6 7
Result: 6.0 × 7.0 = 42.0
```

#### 3.6.4. 除法运算

```bash
$ calc divide 20 4
Result: 20.0 ÷ 4.0 = 5.0
```

#### 3.6.5. 小数运算

```bash
$ calc add 3.14 2.86
Result: 3.14 + 2.86 = 6.0
```

#### 3.6.6. 负数运算

```bash
$ calc subtract -5 3
Result: -5.0 - 3.0 = -8.0
```

#### 3.6.7. 查看帮助

```bash
# 查看主帮助
$ calc --help

Usage: calc [OPTIONS] COMMAND [ARGS]...

  计算器命令行工具 - 一个简单的命令行计算器

  支持基本算术运算：加法、减法、乘法和除法。

Options:
  --version  Show the version and exit.
  --help     Show this message and exit.

Commands:
  add       两个数相加：A + B
  divide    从 A 减去 B：A - B
  multiply  两个数相乘：A × B
  subtract  A 除以 B：A ÷ B

# 查看具体命令帮助
$ calc add --help
```

#### 3.6.8. 查看版本

```bash
$ calc --version
计算器命令行工具 CLI, version 1.0.0
```

#### 9. 错误处理示例

```bash
# 除以零错误
$ calc divide 10 0
Error: 除数不能为零
```

### 3.7、项目结构

```
cli/
├── calculator/              # 主包目录
│   ├── __init__.py         # 包初始化，定义版本信息
│   ├── cli.py              # CLI 命令入口和路由
│   └── calculator.py       # 计算器核心逻辑实现
├── setup.py                # Python 包安装配置
├── install.sh              # 自动化安装脚本
├── uninstall.sh            # 自动化卸载脚本
├── README.md               # 项目文档（本文件）
├── .gitignore              # Git 忽略文件配置
└── LICENSE                 # 开源许可证
```

核心文件说明：     
- **`calculator/__init__.py`**: 包初始化文件，定义版本号和作者信息
- **`calculator/calculator.py`**: 计算器核心类，实现加减乘除四个基本运算
- **`calculator/cli.py`**: CLI 入口文件，使用 Click 框架定义命令行接口
- **`setup.py`**: Python 包配置文件，定义依赖、入口点等元数据
- **`install.sh`**: Bash 安装脚本，自动检查环境并安装
- **`uninstall.sh`**: Bash 卸载脚本，清理安装文件和缓存

### 3.8、开发说明

#### 添加新功能

如果你想添加新的运算功能（例如：幂运算），按照以下步骤：

1. **在 `calculator/calculator.py` 中添加方法**

```python
@staticmethod
def power(a: float, b: float) -> float:
    """计算A的B次方"""
    return a ** b
```

2. **在 `calculator/cli.py` 中添加命令**

```python
@cli.command()
@click.argument('a', type=float)
@click.argument('b', type=float)
def power(a, b):
    """计算A的B次方: A ^ B
    
    示例: calc power 2 3
    """
    result = Calculator.power(a, b)
    click.echo(f"Result: {a} ^ {b} = {result}")
```

3. **重新安装**

```bash
./uninstall.sh
./install.sh
```

#### 代码规范

- 使用类型注解提高代码可读性
- 每个函数都包含 docstring 文档
- 遵循 PEP 8 编码规范
- 使用静态方法组织计算器功能

#### 测试建议

```bash
# 基本功能测试
calc add 1 2
calc subtract 5 3
calc multiply 4 5
calc divide 10 2

# 边界情况测试
calc divide 1 0        # 应该报错
calc add 0 0           # 零值测试
calc multiply -3 -4    # 负数测试
calc add 1.5 2.7       # 小数测试
```

#### 常见问题

##### Q1: 安装后 `calc` 命令找不到？

**A:** 需要将 Python 的 bin 目录添加到 PATH：

```bash
# macOS/Linux
export PATH="$HOME/.local/bin:$PATH"

# 或者找到 Python bin 目录
python3 -m site --user-base
```

##### Q2: 如何更新到新版本？

**A:**

```bash
# 先卸载
./uninstall.sh

# 再安装
./install.sh
```

### 3.9、核心代码

- calculator/__init__.py : 包初始化文件，定义版本号和作者信息

```python
"""
计算器命令行工具 - 一个简单的命令行计算器
"""

__version__ = "1.0.0"
__author__ = "开发者"

```

- calculator/calculator.py : 计算器核心类，实现加减乘除四个基本运算

```python
"""
计算器核心模块，提供基本算术运算功能
"""


class Calculator:
    """简单计算器，支持基本算术运算"""

    @staticmethod
    def add(a: float, b: float) -> float:
        """两个数相加
        
        参数:
            a: 第一个数
            b: 第二个数
            
        返回:
            a 和 b 的和
        """
        return a + b

    @staticmethod
    def subtract(a: float, b: float) -> float:
        """从第一个数减去第二个数
        
        参数:
            a: 第一个数
            b: 第二个数
            
        返回:
            a 和 b 的差
        """
        return a - b

    @staticmethod
    def multiply(a: float, b: float) -> float:
        """两个数相乘
        
        参数:
            a: 第一个数
            b: 第二个数
            
        返回:
            a 和 b 的积
        """
        return a * b

    @staticmethod
    def divide(a: float, b: float) -> float:
        """第一个数除以第二个数
        
        参数:
            a: 被除数
            b: 除数
            
        返回:
            a 除以 b 的商
            
        异常:
            ValueError: 当除数为零时抛出
        """
        if b == 0:
            raise ValueError("除数不能为零")
        return a / b
```

- calculator/cli.py : CLI 入口文件，使用 Click 框架定义命令行接口

```python
"""
计算器应用程序的命令行入口
"""

import click
from calculator.calculator import Calculator
from calculator import __version__


@click.group()
@click.version_option(version=__version__, prog_name="计算器命令行工具")
def cli():
    """计算器命令行工具 - 一个简单的命令行计算器
    
    支持基本算术运算：加法、减法、乘法和除法。
    """
    pass


@cli.command()
@click.argument('a', type=float)
@click.argument('b', type=float)
def add(a, b):
    """两个数相加：A + B
    
    示例: calc add 5 3
    """
    result = Calculator.add(a, b)
    click.echo(f"结果: {a} + {b} = {result}")


@cli.command()
@click.argument('a', type=float)
@click.argument('b', type=float)
def subtract(a, b):
    """从 A 减去 B：A - B
    
    示例: calc subtract 10 4
    """
    result = Calculator.subtract(a, b)
    click.echo(f"结果: {a} - {b} = {result}")


@cli.command()
@click.argument('a', type=float)
@click.argument('b', type=float)
def multiply(a, b):
    """两个数相乘：A × B
    
    示例: calc multiply 6 7
    """
    result = Calculator.multiply(a, b)
    click.echo(f"结果: {a} × {b} = {result}")


@cli.command()
@click.argument('a', type=float)
@click.argument('b', type=float)
def divide(a, b):
    """A 除以 B：A ÷ B
    
    示例: calc divide 20 4
    """
    try:
        result = Calculator.divide(a, b)
        click.echo(f"结果: {a} ÷ {b} = {result}")
    except ValueError as e:
        click.echo(f"错误: {e}", err=True)


def main():
    """CLI 的主入口点"""
    cli()


if __name__ == '__main__':
    main()
```

- setup.py : Python 包配置文件，定义依赖、入口点等元数据

```python
#!/usr/bin/env python
"""
计算器命令行工具的安装配置脚本
"""

from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name="calculator-cli",
    version="1.0.0",
    author="开发者",
    author_email="your.email@example.com",
    description="一个简单的命令行计算器",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/yourusername/calculator-cli",
    packages=find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Environment :: Console",
    ],
    python_requires=">=3.6",
    install_requires=[
        "click>=8.0.0",
    ],
    entry_points={
        "console_scripts": [
            "calc=calculator.cli:main",
        ],
    },
)
```

- install.sh: Bash 安装脚本，自动检查环境并安装

```python
#!/bin/bash

# 计算器命令行工具安装脚本
# 此脚本用于安装计算器 CLI 应用程序

set -e  # 遇到错误时退出

echo "========================================="
echo "计算器命令行工具 - 安装程序"
echo "========================================="
echo ""

# 检查是否安装了 Python
if ! command -v python3 &> /dev/null; then
    echo "错误: 未安装 Python 3。"
    echo "请先安装 Python 3.6 或更高版本。"
    exit 1
fi

echo "✓ 找到 Python: $(python3 --version)"

# 检查是否安装了 pip
if ! command -v pip3 &> /dev/null; then
    echo "错误: 未安装 pip3。"
    echo "请先安装 pip。"
    exit 1
fi

echo "✓ 找到 pip: $(pip3 --version)"
echo ""

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# 进入项目目录
cd "$SCRIPT_DIR"

echo "正在安装依赖..."
pip3 install click>=8.0.0
echo "✓ 依赖安装完成"
echo ""

echo "正在安装计算器命令行工具..."
pip3 install -e .
echo "✓ 计算器命令行工具安装成功"
echo ""

# 验证安装
if command -v calc &> /dev/null; then
    echo "========================================="
    echo "安装完成！"
    echo "========================================="
    echo ""
    echo "现在可以使用计算器了："
    echo "  calc --help          # 显示帮助信息"
    echo "  calc add 5 3         # 两个数相加"
    echo "  calc subtract 10 4   # 两个数相减"
    echo "  calc multiply 6 7    # 两个数相乘"
    echo "  calc divide 20 4     # 两个数相除"
    echo ""
    echo "版本: $(calc --version)"
else
    echo "警告: 安装完成，但在 PATH 中找不到 'calc' 命令。"
    echo "您可能需要将 Python 的 bin 目录添加到 PATH 环境变量中。"
fi
```

- uninstall.sh: Bash 卸载脚本，清理安装文件和缓存

```python
#!/bin/bash

# 计算器命令行工具卸载脚本
# 此脚本用于卸载计算器 CLI 应用程序

set -e  # 遇到错误时退出

echo "========================================="
echo "计算器命令行工具 - 卸载程序"
echo "========================================="
echo ""

# 检查包是否已安装
if ! pip3 show calculator-cli &> /dev/null; then
    echo "计算器命令行工具未安装。"
    exit 0
fi

echo "正在卸载计算器命令行工具..."
pip3 uninstall -y calculator-cli
echo "✓ 计算器命令行工具已卸载"
echo ""

# 清理剩余文件
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILD_DIRS=("build" "dist" "*.egg-info" "__pycache__")

for dir in "${BUILD_DIRS[@]}"; do
    if [ -d "$SCRIPT_DIR/$dir" ]; then
        echo "正在删除 $dir..."
        rm -rf "$SCRIPT_DIR/$dir"
    fi
done

# 递归删除 __pycache__ 目录
find "$SCRIPT_DIR" -type d -name "__pycache__" -exec rm -rf {} + 2>/dev/null || true

echo "✓ 清理完成"
echo ""
echo "========================================="
echo "卸载完成！"
echo "========================================="

```

