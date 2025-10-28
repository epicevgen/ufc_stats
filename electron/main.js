const { app, BrowserWindow, Menu, shell, dialog, ipcMain } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const fs = require('fs');

// Настройки приложения
const APP_CONFIG = {
  name: 'UFC Stats',
  version: '1.0.0',
  javaAppPort: 8080,
  javaAppPath: (() => {
    // Проверяем разные возможные пути к JAR файлу
    const possiblePaths = [
      path.join(__dirname, '../app/ufc-stats-1.0.0.jar'),      // В нативном приложении
      path.join(__dirname, '../build/libs/ufc-stats-1.0.0.jar'), // В режиме разработки
      path.join(__dirname, 'app/ufc-stats-1.0.0.jar'),        // Альтернативный путь
      path.join(__dirname, 'build/libs/ufc-stats-1.0.0.jar')  // Альтернативный путь
    ];
    
    for (const jarPath of possiblePaths) {
      if (fs.existsSync(jarPath)) {
        console.log('✅ JAR файл найден:', jarPath);
        return jarPath;
      }
    }
    
    console.error('❌ JAR файл не найден ни по одному из путей:', possiblePaths);
    return possiblePaths[0]; // Возвращаем первый путь для отображения ошибки
  })(),
  dataPath: path.join(__dirname, '../data'),
  scriptsPath: path.join(__dirname, '../scripts')
};

let mainWindow;
let javaProcess;

// Создание главного окна
function createWindow() {
  // Проверяем, что app готов
  if (!app.isReady()) {
    console.error('❌ Попытка создать окно до готовности app');
    return;
  }
  
  console.log('🪟 Создание главного окна...');
  mainWindow = new BrowserWindow({
    width: 1200,
    height: 800,
    minWidth: 800,
    minHeight: 600,
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
      enableRemoteModule: false,
      preload: path.join(__dirname, 'preload.js')
    },
    icon: path.join(__dirname, '../assets/icon.png'),
    title: APP_CONFIG.name,
    show: false, // Не показываем окно до полной загрузки
    titleBarStyle: 'default'
  });

  // Загружаем приложение после запуска Java
  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
    console.log('UFC Stats Desktop приложение запущено');
  });

  // Обработка закрытия окна
  mainWindow.on('closed', () => {
    mainWindow = null;
  });

  // Обработка внешних ссылок
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: 'deny' };
  });

  // Настройка меню
  createMenu();
}

// Создание меню приложения
function createMenu() {
  const template = [
    {
      label: 'Файл',
      submenu: [
        {
          label: 'Создать резервную копию',
          accelerator: 'CmdOrCtrl+B',
          click: () => {
            createBackup();
          }
        },
        {
          label: 'Восстановить из бэкапа',
          click: () => {
            restoreFromBackup();
          }
        },
        { type: 'separator' },
        {
          label: 'Выход',
          accelerator: process.platform === 'darwin' ? 'Cmd+Q' : 'Ctrl+Q',
          click: () => {
            app.quit();
          }
        }
      ]
    },
    {
      label: 'Просмотр',
      submenu: [
        {
          label: 'Перезагрузить',
          accelerator: 'CmdOrCtrl+R',
          click: () => {
            if (mainWindow) {
              mainWindow.reload();
            }
          }
        },
        {
          label: 'Полный экран',
          accelerator: 'F11',
          click: () => {
            if (mainWindow) {
              mainWindow.setFullScreen(!mainWindow.isFullScreen());
            }
          }
        },
        {
          label: 'Инструменты разработчика',
          accelerator: 'F12',
          click: () => {
            if (mainWindow) {
              mainWindow.webContents.toggleDevTools();
            }
          }
        }
      ]
    },
    {
      label: 'Справка',
      submenu: [
        {
          label: 'О программе',
          click: () => {
            showAboutDialog();
          }
        },
        {
          label: 'Документация',
          click: () => {
            shell.openExternal('https://github.com/epicevgen/ufc_stats');
          }
        }
      ]
    }
  ];

  const menu = Menu.buildFromTemplate(template);
  Menu.setApplicationMenu(menu);
}

// Запуск Java приложения
function startJavaApp() {
  return new Promise((resolve, reject) => {
    console.log('Запуск Java приложения...');
    
    // Проверяем существование JAR файла
    if (!fs.existsSync(APP_CONFIG.javaAppPath)) {
      console.error('JAR файл не найден:', APP_CONFIG.javaAppPath);
      reject(new Error('JAR файл не найден. Сначала соберите проект: ./gradlew bootJar'));
      return;
    }

    // Запускаем Java приложение
    javaProcess = spawn('java', [
      '-jar',
      APP_CONFIG.javaAppPath,
      '--server.port=' + APP_CONFIG.javaAppPort
    ], {
      cwd: path.dirname(APP_CONFIG.javaAppPath),
      stdio: 'pipe'
    });

    javaProcess.stdout.on('data', (data) => {
      const output = data.toString();
      console.log('Java App:', output);
      
      // Проверяем, что приложение запустилось
      if (output.includes('Started UfcStatsApplication') || output.includes('Tomcat started on port')) {
        console.log('Java приложение успешно запущено');
        resolve();
      }
    });

    javaProcess.stderr.on('data', (data) => {
      console.error('Java App Error:', data.toString());
    });

    javaProcess.on('error', (error) => {
      console.error('Ошибка запуска Java приложения:', error);
      reject(error);
    });

    javaProcess.on('exit', (code) => {
      console.log('Java приложение завершено с кодом:', code);
    });

    // Таймаут для запуска
    setTimeout(() => {
      resolve();
    }, 10000);
  });
}

// Создание резервной копии
async function createBackup() {
  try {
    const { exec } = require('child_process');
    const backupScript = path.join(APP_CONFIG.scriptsPath, 'backup-db.sh');
    
    if (!fs.existsSync(backupScript)) {
      dialog.showErrorBox('Ошибка', 'Скрипт резервного копирования не найден');
      return;
    }

    exec(`bash "${backupScript}" backup`, (error, stdout, stderr) => {
      if (error) {
        dialog.showErrorBox('Ошибка резервного копирования', error.message);
        return;
      }
      
      dialog.showMessageBox(mainWindow, {
        type: 'info',
        title: 'Резервное копирование',
        message: 'Резервная копия создана успешно!',
        detail: stdout
      });
    });
  } catch (error) {
    dialog.showErrorBox('Ошибка', error.message);
  }
}

// Восстановление из резервной копии
async function restoreFromBackup() {
  try {
    const result = await dialog.showOpenDialog(mainWindow, {
      title: 'Выберите файл резервной копии',
      filters: [
        { name: 'ZIP архивы', extensions: ['zip'] },
        { name: 'Все файлы', extensions: ['*'] }
      ],
      properties: ['openFile']
    });

    if (!result.canceled && result.filePaths.length > 0) {
      const backupFile = result.filePaths[0];
      const { exec } = require('child_process');
      const backupScript = path.join(APP_CONFIG.scriptsPath, 'backup-db.sh');
      
      exec(`bash "${backupScript}" restore "${backupFile}"`, (error, stdout, stderr) => {
        if (error) {
          dialog.showErrorBox('Ошибка восстановления', error.message);
          return;
        }
        
        dialog.showMessageBox(mainWindow, {
          type: 'info',
          title: 'Восстановление',
          message: 'База данных восстановлена успешно!',
          detail: 'Приложение будет перезапущено для применения изменений.'
        });
        
        // Перезагружаем страницу
        if (mainWindow) {
          mainWindow.reload();
        }
      });
    }
  } catch (error) {
    dialog.showErrorBox('Ошибка', error.message);
  }
}

// Диалог "О программе"
function showAboutDialog() {
  dialog.showMessageBox(mainWindow, {
    type: 'info',
    title: 'О программе',
    message: APP_CONFIG.name,
    detail: `Версия: ${APP_CONFIG.version}\n\nНативное приложение для отслеживания статистики боев UFC 5\n\nРазработано с использованием Electron и Spring Boot`
  });
}

// Проверяем, что мы запущены в Electron
if (typeof app === 'undefined') {
  console.error('❌ Этот файл должен запускаться через Electron, а не через Node.js');
  console.log('💡 Используйте: npm run electron');
  process.exit(1);
}

// Обработка событий приложения
app.whenReady().then(async () => {
  try {
    console.log('🚀 Запуск UFC Stats Desktop приложения...');
    
    // Запускаем Java приложение
    await startJavaApp();
    
    // Создаем окно
    createWindow();
    
    // Загружаем приложение
    await mainWindow.loadURL(`http://localhost:${APP_CONFIG.javaAppPort}`);
    
    console.log('✅ Приложение успешно запущено!');
    
  } catch (error) {
    console.error('❌ Ошибка запуска приложения:', error);
    if (dialog && dialog.showErrorBox) {
      dialog.showErrorBox('Ошибка запуска', error.message);
    }
    if (app && app.quit) {
      app.quit();
    }
  }
});

// Обработка закрытия приложения
app.on('window-all-closed', () => {
  // Завершаем Java процесс
  if (javaProcess) {
    javaProcess.kill();
  }
  
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});

// Обработка закрытия приложения
app.on('before-quit', () => {
  if (javaProcess) {
    javaProcess.kill();
  }
});

// IPC обработчики
ipcMain.handle('get-app-info', () => {
  return {
    name: APP_CONFIG.name,
    version: APP_CONFIG.version,
    platform: process.platform
  };
});

ipcMain.handle('create-backup', async () => {
  try {
    const { exec } = require('child_process');
    const backupScript = path.join(APP_CONFIG.scriptsPath, 'backup-db.sh');
    
    return new Promise((resolve, reject) => {
      exec(`bash "${backupScript}" backup`, (error, stdout, stderr) => {
        if (error) {
          reject(error);
        } else {
          resolve(stdout);
        }
      });
    });
  } catch (error) {
    throw error;
  }
});

console.log('UFC Stats Desktop приложение инициализировано');
