

springmvc的resturl是通过@RequestMapping 及@PathVariable annotation提供的, 通过如@RequestMapping(value="/blog /{id}",method=RequestMethod.DELETE)即可处理/blog/1 的delete请求.

```
@RequestMapping(value="/blog/{id}",method=RequestMethod.DELETE)
public ModelAndView delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)　{
    blogManager.removeById(id);
    return new ModelAndView(LIST_ACTION);
}

// 基于kiss原则：为了提高易用性和灵活性，推荐不要使用methos属性；

```
