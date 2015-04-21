/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.data.manager.PreferencesManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;

import com.uwsoft.editor.mvc.event.MenuItemListener;
import com.uwsoft.editor.renderer.data.SceneVO;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ArrayList;


public class Overlap2DMenuBar extends MenuBar {
    public static final String FILE_MENU = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".FILE_MENU";
    public static final String NEW_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".NEW_PROJECT";
    public static final String OPEN_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".OPEN_PROJECT";
    public static final String SAVE_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".SAVE_PROJECT";
    public static final String IMPORT_TO_LIBRARY = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".IMPORT_TO_LIBRARY";
    public static final String RECENT_PROJECTS = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".RECENT_PROJECTS";
    public static final String EXPORT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXPORT";
    public static final String EXPORT_SETTINGS = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXPORT_SETTINGS";
    public static final String EXIT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXIT";
    public static final String NEW_SCENE = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".NEW_SCENE";
    public static final String SELECT_SCENE = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".SELECT_SCENE";
    public static final String DELETE_CURRENT_SCENE = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".DELETE_CURRENT_SCENE";
    //
    public static final String EDIT_MENU = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EDIT_MENU";
    public static final String CUT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".CUT";
    public static final String COPY = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".COPY";
    public static final String PAST = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".PAST";
    public static final String UNDO = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".UNDO";
    public static final String REDO = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".REDO";
    //
    private static final String TAG = Overlap2DMenuBar.class.getCanonicalName();
    private final FileMenu fileMenu;
    private final String maskKey;
    private final EditMenu editMenu;
    private final Overlap2DFacade facade;

    public Overlap2DMenuBar() {
        facade = Overlap2DFacade.getInstance();
        maskKey = SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC ? "Cmd" : "Ctrl";
        fileMenu = new FileMenu();
        editMenu = new EditMenu();
        addMenu(fileMenu);
        addMenu(editMenu);
    }


    public void addScenes(ArrayList<SceneVO> scenes) {
        fileMenu.addScenes(scenes);
    }

    public void reInitScenes(ArrayList<SceneVO> scenes) {
        fileMenu.reInitScenes(scenes);
    }

    public void reInitRecent(ArrayList<String> paths) {
        fileMenu.reInitRecent(paths);
    }

    public void setProjectOpen(boolean open) {
        fileMenu.setProjectOpen(open);
        editMenu.setProjectOpen(open);
    }

    private class EditMenu extends Menu {


        private final MenuItem cut;
        private final MenuItem copy;
        private final MenuItem paste;
        private final MenuItem undo;
        private final MenuItem redo;

        public EditMenu() {
            super("Edit");
            pad(5);
            cut = new MenuItem("Cut", new MenuItemListener(CUT, null, EDIT_MENU)).setShortcut(maskKey + " + X");
            copy = new MenuItem("Copy", new MenuItemListener(COPY, null, EDIT_MENU)).setShortcut(maskKey + " + C");
            paste = new MenuItem("Paste", new MenuItemListener(PAST, null, EDIT_MENU)).setShortcut(maskKey + " + P");
            undo = new MenuItem("Undo", new MenuItemListener(UNDO, null, EDIT_MENU)).setShortcut(maskKey + " + Z");
            redo = new MenuItem("Redo", new MenuItemListener(REDO, null, EDIT_MENU)).setShortcut(maskKey + " + Y");
            addItem(cut);
            addItem(copy);
            addItem(paste);
            addItem(undo);
            addItem(redo);
        }

        public void setProjectOpen(boolean open) {
            cut.setDisabled(!open);
            copy.setDisabled(!open);
            paste.setDisabled(!open);
            undo.setDisabled(!open);
            redo.setDisabled(!open);
        }

    }


    private class FileMenu extends Menu {

        private final PopupMenu scenesPopupMenu;
        private final Array<MenuItem> sceneMenuItems;
        private final MenuItem saveProject;
        private final MenuItem scenesMenuItem;
        private final MenuItem importToLibrary;
        private final MenuItem export;
        private final MenuItem exportSettings;

        private final PopupMenu recentProjectsPopupMenu;
        private final Array<MenuItem> recentProjectsMenuItems;
        private final MenuItem recentProjectsMenuItem;

        public FileMenu() {
            super("File");
            pad(5);
            saveProject = new MenuItem("Save Project", new MenuItemListener(SAVE_PROJECT, null, FILE_MENU));
            addItem(new MenuItem("New Project", new MenuItemListener(NEW_PROJECT, null, FILE_MENU)));
            addItem(new MenuItem("Open Project", new MenuItemListener(OPEN_PROJECT, null, FILE_MENU)));
            addItem(saveProject);
            //
            scenesMenuItem = new MenuItem("Scenes");
            scenesPopupMenu = new PopupMenu();

            scenesMenuItem.setSubMenu(scenesPopupMenu);
            addItem(scenesMenuItem);
            //
            addSeparator();
            importToLibrary = new MenuItem("Import to Library", new MenuItemListener(IMPORT_TO_LIBRARY, null, FILE_MENU));
            export = new MenuItem("Export", new MenuItemListener(EXPORT, null, FILE_MENU));
            exportSettings = new MenuItem("Export Settings", new MenuItemListener(EXPORT_SETTINGS, null, FILE_MENU));
            addItem(importToLibrary);
            addItem(export);
            addItem(exportSettings);
            //
            addSeparator();
            recentProjectsMenuItem = new MenuItem("Recent Projects...");
            recentProjectsPopupMenu = new PopupMenu();
            recentProjectsMenuItem.setSubMenu(recentProjectsPopupMenu);
            addItem(recentProjectsMenuItem);
            recentProjectsMenuItems = new Array<>();
            PreferencesManager prefs = PreferencesManager.getInstance();
            prefs.buildRecentHistory();
            addRecent(prefs.getRecentHistory());
            //
            addSeparator();
            addItem(new MenuItem("Exit", new MenuItemListener(EXIT, FILE_MENU)));
            sceneMenuItems = new Array<>();
        }

        public void addScenes(ArrayList<SceneVO> scenes) {
            for (SceneVO sceneVO : scenes) {
                MenuItem menuItem = new MenuItem(sceneVO.sceneName, new MenuItemListener(SELECT_SCENE, sceneVO.sceneName, FILE_MENU));
                sceneMenuItems.add(menuItem);
                scenesPopupMenu.addItem(menuItem);
            }
        }

        public void reInitScenes(ArrayList<SceneVO> scenes) {
            sceneMenuItems.clear();
            scenesPopupMenu.clear();
            scenesPopupMenu.addItem(new MenuItem("Create New Scene", new MenuItemListener(NEW_SCENE, null, FILE_MENU)));
            scenesPopupMenu.addItem(new MenuItem("Delete Current Scene", new MenuItemListener(DELETE_CURRENT_SCENE, null, FILE_MENU)));
            scenesPopupMenu.addSeparator();
            addScenes(scenes);
        }

        public String getFolderName(String path) {
            File path1 = new File(path);
            File path2 = new File(path1.getParent());
            return path2.getName();
        }

        public void addRecent(ArrayList<String> paths) {
            for (String path : paths) {
                MenuItem menuItem = new MenuItem(getFolderName(path), new MenuItemListener(RECENT_PROJECTS, path, FILE_MENU));
                recentProjectsMenuItems.add(menuItem);
                recentProjectsPopupMenu.addItem(menuItem);
            }
        }

        public void reInitRecent(ArrayList<String> paths) {
            recentProjectsMenuItems.clear();
            recentProjectsPopupMenu.clear();

            addRecent(paths);
        }

        public void setProjectOpen(boolean open) {
            saveProject.setDisabled(!open);
            scenesMenuItem.setDisabled(!open);
            importToLibrary.setDisabled(!open);
            export.setDisabled(!open);
            exportSettings.setDisabled(!open);
        }

//        private class RecentProjectListener extends ChangeListener {
//            private final String path;
//
//            public RecentProjectListener(String path) {
//                this.path = path;
//            }
//
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Gdx.app.log(TAG, "recentProject : " + path);
//                mediator.recentProjectItemClicked(path);
//            }
//        }
    }



}
