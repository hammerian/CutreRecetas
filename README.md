# CutreRecetas
---

 PRÁCTICA ANDROID STUDIO:
 
> Diseño de la Interfaz de Usuario:

● Utiliza un RelativeLayout o LinearLayout para crear la interfaz principal de la aplicación.

● Implementa un menú lateral de navegación (DrawerNavigationView) con opciones como "Configuración" y "Acerca de".

> Lista de Tareas:

● Utiliza un RecyclerView para mostrar la lista de tareas.

● Cada elemento de la lista debe contener una casilla de verificación (CheckBox) para marcar tareas como completadas o pendientes.

● Implementa la funcionalidad para eliminar tareas de la lista al deslizarlas hacia un lado (usando ItemTouchHelper).

● Al hacer clic en un elemento de la lista, se debe mostrar un AlertDialog que permita editar la tarea.

> Agregar Tareas:

● Agrega un botón o ícono de "Agregar Tarea" en la parte inferior de la pantalla.

● Al hacer clic en este botón, muestra un cuadro de diálogo personalizado con un EditText para ingresar una nueva tarea.

● Al agregar una nueva tarea, esta debe aparecer en la lista.
 Filtros y Categorías:

● Implementa un Spinner en la parte superior de la lista de tareas para permitir a los usuarios filtrar las tareas por categoría (por ejemplo, trabajo, estudio, personal).

● Utiliza SharedPreferences para almacenar la selección del filtro y restaurarla al reiniciar la aplicación.

> Galería de Imágenes:

● Agrega un botón que permita a los usuarios seleccionar una imagen de la galería.

● Implementa la funcionalidad de acceso a permisos para acceder a la galería de imágenes y seleccionar una foto.

● Muestra la imagen seleccionada junto a la tarea en la lista.

> Almacenamiento de Datos:

● Utiliza SharedPreferences para almacenar las tareas creadas por el usuario y su estado (completado o pendiente).

● Al cerrar y volver a abrir la aplicación, las tareas deben cargarse desde SharedPreferences.
