/**
 * 博客悬浮目录（自包含 · 引入即自动初始化）
 *
 * 为 editor.md 渲染出的文内目录（.markdown-toc-list）提供「滚动时常驻右下角」的目录浮层，
 * 并修正 editor.md 生成的目录锚点（href="#标题文本"）与标题真实 id（"hN-slug"）不匹配的问题。
 *
 * 用法：直接引入即可，渲染完成后自动初始化，无需任何手动调用。
 *     <script src="../page/static/js/toc-float.js"></script>
 *
 * 需要自定义容器时（可选）：
 *     window.TocFloat.init({ contentSelector: "#my-content" });
 *
 * @author xuxueli 2026-10-18
 */
(function (window, $) {
    "use strict";

    if (!$) {
        return;
    }

    /** 默认文章内容容器；不存在时回退为整篇文档 */
    var DEFAULT_CONTENT = "#blog-content-view";

    /** 样式（自包含，注入一次） */
    var CSS =
        "#toc-float{position:fixed;right:20px;bottom:70px;z-index:99998;}" +
        "#toc-float-btn{display:none;width:40px;height:40px;line-height:40px;text-align:center;color:#fff;" +
        "background-color:#222d32;border-radius:5px;cursor:pointer;opacity:.7;box-shadow:0 2px 6px rgba(0,0,0,.25);}" +
        "#toc-float-btn:hover{opacity:1;}" +
        "#toc-float-btn .material-icons{font-size:24px;vertical-align:middle;}" +
        "#toc-float-panel{display:none;position:absolute;right:0;bottom:50px;width:260px;max-width:calc(100vw - 40px);" +
        "max-height:60vh;overflow-y:auto;padding:10px 12px;background:#fff;border:1px solid #ddd;border-radius:6px;" +
        "box-shadow:0 4px 16px rgba(0,0,0,.18);}" +
        "#toc-float-panel ul{margin:0;padding-left:0;}" +
        "#toc-float-panel li{list-style:none;}" +
        "#toc-float-panel ul ul{padding-left:14px;}" +
        "#toc-float-panel a{display:block;padding:4px 6px;color:#444;font-size:13px;line-height:1.5;border-radius:3px;" +
        "text-decoration:none;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}" +
        "#toc-float-panel a:hover{background:#f2f2f2;color:#26a69a;}";

    /** 是否已初始化（避免重复绑定事件） */
    var initialized = false;

    /** 注入样式（幂等） */
    function injectStyle() {
        if (document.getElementById("toc-float-style")) {
            return;
        }
        $("<style id='toc-float-style'></style>").text(CSS).appendTo("head");
    }

    /** 解析作用域：容器存在则用容器，否则回退整篇文档 */
    function scopeOf(contentSelector) {
        var root = $(contentSelector || DEFAULT_CONTENT);
        return root.length ? root : $(document);
    }

    /** 修正目录锚点：editormd 的 href 为 "#标题文本"，标题真实 id 为 "hN-slug"，按文档顺序一一对应重写 */
    function fixAnchors(contentSelector) {
        var root = scopeOf(contentSelector);
        var heads = root.find("h1,h2,h3,h4,h5,h6").filter(function () {
            return !!this.id;
        });
        if (!heads.length) {
            return;
        }
        root.find(".markdown-toc-list").each(function () {
            var links = $(this).find("a");
            if (links.length !== heads.length) {   // 数量不一致则跳过，避免误改
                return;
            }
            links.each(function (i) {
                this.setAttribute("href", "#" + heads[i].id);
            });
        });
    }

    /** 取正文中「可见且有内容」的内联目录（排除隐藏的 toc 下拉菜单），无则返回空 */
    function findList(contentSelector) {
        var root = scopeOf(contentSelector);
        var list = root.find(".markdown-toc-list").filter(function () {
            return $(this).find("li").length && $(this).is(":visible");
        }).first();
        if (!list.length) {
            list = root.find(".markdown-toc-list").filter(function () {
                return $(this).find("li").length;
            }).first();
        }
        return list;
    }

    /**
     * 初始化悬浮目录（幂等）
     *
     * @param {Object} [options]
     * @param {string} [options.contentSelector="#blog-content-view"] 文章内容容器选择器
     * @returns {boolean} 是否已成功初始化（目录尚未渲染时返回 false）
     */
    function init(options) {
        if (initialized) {
            return true;
        }
        options = options || {};
        injectStyle();
        fixAnchors(options.contentSelector);

        var list = findList(options.contentSelector);
        if (!list.length) {
            return false;
        }

        var box = $('<div id="toc-float"></div>');
        var panel = $('<div id="toc-float-panel"></div>');
        var btn = $('<div id="toc-float-btn" title="文章目录"><i class="material-icons">list</i></div>');
        // 克隆「整个 ul」（保留外层 <ul>，层级才不会塌），而非其 innerHTML
        panel.append(list.clone());
        panel.find("[id]").removeAttr("id");   // 避免与正文重复 id
        box.append(panel).append(btn).appendTo("body");

        // 展开/收起
        btn.on("click", function (e) {
            e.stopPropagation();
            panel.toggle();
        });
        // 点击空白处关闭
        $(document).on("click", function () {
            panel.hide();
        });
        // 点击目录项后关闭浮层（跳转交由浏览器锚点处理）
        panel.on("click", "a", function () {
            panel.hide();
        });

        // 滚动离开文内目录后显示入口
        $(window).on("scroll", function () {
            var tocBottom = list.offset().top + list.outerHeight();
            if ($(window).scrollTop() > tocBottom) {
                btn.fadeIn(200);
            } else {
                btn.fadeOut(200);
                panel.hide();
            }
        });

        initialized = true;
        return true;
    }

    /** 自动初始化：已渲染则立即处理，否则监听 DOM，待目录出现后再初始化 */
    function autoInit() {
        if (init()) {
            return;
        }
        // 目录由 editor.md 异步渲染，监听 DOM 变化，出现后立即初始化并停止监听
        if (window.MutationObserver && document.body) {
            var observer = new MutationObserver(function () {
                if (init()) {
                    observer.disconnect();
                }
            });
            observer.observe(document.body, { childList: true, subtree: true });
            // 兜底：30 秒后停止监听，避免长驻
            setTimeout(function () {
                observer.disconnect();
            }, 30000);
        } else {
            // 兜底：老浏览器轮询
            var timer = setInterval(function () {
                if (init()) {
                    clearInterval(timer);
                }
            }, 500);
        }
    }

    /** 对外接口（自动执行之外，仍可手动调用以自定义容器） */
    window.TocFloat = {
        init: init,
        fixAnchors: fixAnchors,
        autoInit: autoInit
    };

    // 引入即自动执行：DOM 就绪后开始尝试
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", autoInit);
    } else {
        autoInit();
    }

})(window, window.jQuery);
