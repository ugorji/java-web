We will not use a framework, but rather a strategy, to do this.

The only dependencies will be:
- freemarker

Everyone will only work with WebInteractionContext (not httpservletrequest, etc)

ContextListener
- put the following in the ApplicationContext
  - TemplateHandler, 
Servlet
- create WebInteractionContext (and put on thread)
- decipher Subject (and put on thread)
- create TemplateContext (and put on thread)
- create ViewContext, and set Locale (and put on thread)
- find TemplateHandler (and put on thread)
- decipher WebAction (by peeping for action from request params, request path)
- call Action.processAction
  - if redirect_after_post is in status, redirect
  - else call Action.render

- finally,
  - take ViewContext and put in session (for later rendering of last view)
  
Action
- processAction()
  - populate ViewContext
  - do work (based on ViewContext)
  - if not supporting rendering, then
    - put info necessary for rendering into ActionView (changing the action key)
    - return redirect_after_post

- render()
  - leverages ViewContext
  - if doing render yourself (e.g. for RSS, attachment viewing, etc)
    - populate TemplateContext
    - call TemplateHandler.render();
  - *** optionally change ViewContext to reflect next course of action

TemplateHandler
- leverages ViewContext, TemplateContext
- render()

TemplateContext
- setAttribute(String, Object model)
- getAttribute(String)
- addModel(String, Object model)

ViewContext
- getName()
- setName(String)
- setAttribute(String, Object)
- getAttribute(String)

Handling actions:
- each action leverages the following for rendering:
  - Model object (a POJO)
  - a freemarker template file 
- all template files are stored in a given directory
  - $actionName.head.html
  - $actionName.body.html
  - $template.html

Every action only does one thing
- e.g. delete_page, edit_file, preview_file, etc

To do this:
- make a forum2 module, and use that???

By default, we let each action store a model using its name
e.g. calendar action will do putModel("calendar", CalendarModelObject)

This way, many different actions can have their models in, and a template
could actually encapsulate different models together

Application Extensibility
==========================

Misc design notes
=================

The ViewContext
- stores information used to recreate a current view
- this includes information that the TemplateHandler can use 
  - e.g. information about borders, etc

Note:
- If you make actions who return the flag REDIRECT_AFTER_POST,
  - ensure that the view context sets its action to where to redirect
  - the servlet just does a redirect (nothing is stored in session)
    - so the new action should be self sufficient 
      (not depend on anything stored in a model from the referring action)

If a parameter starts with "action.", then it is not a render parameter.


