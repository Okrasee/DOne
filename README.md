# DOne
I learnt Android development to go beyond ux design and building a GTD (getting things done) app is a good kickoff for practice.

My GTD app, named 'DOne', has two major features:
1. group similar to-dos under the same category;
2. sort them by time or by category.

## My Role
Sketching, Prototyping, User interface design, User study, Android development

## Initial prototype
The layout and content of the top corers is inspired by Mike Zhou’s “Listify”. Each task is displayed in a card form with a colored title and a description. Clicking on the title bar would fold/expand the description section.


## Features & Refinement
### * Categorization

After receiving feedback through user testing, I decided to categorize the tasks rather than throwing all sorts of tasks onto the same screen. I implemented this feature by the use of **Expandable ListView**. Each category is displayed as a group view and its to-dos are child views. One could click on the category view to expand/fold the to-dos to make the interface more concise and easier to browse through.
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/low-fi.png" alt="alt text" height = "500">
</p>

### * Sorting
Under each category, the tasks are sorted by the order of their deadlines. For instance, under the “study” category, “numerical” is listed above “programming” which indicates that “numerical” should be finished prior to “programming”.

When I used other GTD app, I found the latest added to-do is simply put onto the end of the list despite that its deadline may be the earliest. This may lead to an issue that people need to browse through the whole list to find out what to do first. In a worse case, people may miss out something if they do not do a careful check. So I believe a sorting feature is fairly important as it enables people to avoid manual check.
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/sorting.png" alt="alt text" height = "500">
</p>

### * Floating Action Button
As I added more features to the app, I found an “adding” FAB is not adequate. However, placing five floating action buttons on the main interface is not appropriate, either. So I created an expandable FAB menu.
<p align="center">
<img src="https://github.com/Okrasee/DOne/blob/master/FAB.gif" alt="alt text" height = "500">
</p>

### * Add a category/task
A new category is created via a popup window. Referring to Mike Zhou’s “Listify”, I added a color palette such that the user could pick a color for each category to make distinguishable. As an art lover, I picked all the colors from the Morandi color palette.

One could add a task similarly. A task consists of four parts: title, description, due date and due time. It would be overwhelming to put all the information in one screen. So I implemented a swiping view such that user could swipe to fill in the description, pick the due date and select the due time.
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/add_category_1.png" alt="alt text" height = "500">
</p>
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/add_category_2.png" alt="alt text" height = "500">
</p>

### * Edit/Finish/Delete
It is necessary to make the to-dos editable in case there is any change. The user may also want to checkmark the to-do if he/she has finished it; or remove it if he/she does not want it any more. So I made the task views **swipable**: one could swipe left to see the hidden menu. Clicking on either the “checkmark” or “delete” button would pop up a window to allow double check and avoid mistakes. If one has finished a task, it would be transferred to the completed list. The user could also check the completed list if necessary.

Likewise, users may need to edit their category content or even delete the category. To differentiate the category view from task view, I added a hidden menu. By clicking on the rightmost button, the hidden menu would show up/hide away.
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/edit.png" alt="alt text" height = "500">
</p>

### * Multiple modes
The categorized to-do list brings an issue that the user may not get a sense of the order of all tasks from different categories. So I added a mixed mode in which the task are sorted by order to time regardless of their categories. Their background color is correspondent to the color of their category. The mixed task list and the categorized task list are synchronized. Whatever changes made to the task in the categorized list will also be updated in the mixed list.

The “refresh” button enables the user to clear the tasks which deadlines have already passed the current time from the list.
<p align = "center">
<img src="https://github.com/Okrasee/DOne/blob/master/multiple_mode.png" alt="alt text" height = "500">
</p>

## Reflection
GTD App is one of the most fundamental, commonly used app. Nevertheless, it takes time to come up with an ergonomic, easy-to-use version. I have enriched my experience of integrating designing and developing through building this app. I will continue to refine and improve it.

## Improvements
* I will add instruction pages to inform users about the features of the app. The instruction pages will show up before the main interface is loaded.
* The reminding function could have a “Repeat” feature. For example, if a math course homework is due every Wednesday before class, the user could add the task and select “Repeat — Every Wednesday”.
* In my personal opinion, sending out reminding notification before deadline is too late and thus unnecessary. However, I will conduct user study to figure out how the majority thinks.
* When my HCI research mentor saw my app, he recommended adding a feature such that the user could mark some specific categories/tasks as important. I will figure out a way to sort the tasks by order of importance.


