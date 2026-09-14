# Guía Completa de Comandos Git

## Configuración Inicial

### Configurar identidad
```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu.email@example.com"
```

### Ver configuración actual
```bash
git config --list
git config user.name
git config user.email
```

---

## Clonar Repositorio

### Clonar un repositorio
```bash
git clone <URL_del_repositorio>
git clone <URL_del_repositorio> <nombre_carpeta>
```

### Clonar una rama específica
```bash
git clone -b <nombre_rama> <URL_del_repositorio>
```

---

## Ramas (Branches)

### Listar ramas locales
```bash
git branch
git branch -v  # Con más detalles
```

### Listar todas las ramas (local y remoto)
```bash
git branch -a
git branch -av  # Con más detalles
```

### Crear una rama
```bash
git branch <nombre_rama>
```

### Crear una rama basada en otra rama
```bash
git branch <nombre_rama> <rama_base>
```

### Cambiar a una rama
```bash
git switch <nombre_rama>
git checkout <nombre_rama>  # (método antiguo)
```

### Crear y cambiar a una rama simultáneamente
```bash
git switch -c <nombre_rama>
git checkout -b <nombre_rama>  # (método antiguo)
```

### Cambiar a una rama remota (rastrearla)
```bash
git switch <nombre_rama>
git switch --track origin/<nombre_rama>
```

### Renombrar una rama
```bash
git branch -m <nombre_actual> <nombre_nuevo>
git branch -m <nombre_nuevo>  # Si estás en la rama actual
```

### Eliminar una rama local
```bash
git branch -d <nombre_rama>  # Si está merged
git branch -D <nombre_rama>  # Fuerza la eliminación
```

### Eliminar una rama remota
```bash
git push origin --delete <nombre_rama>
git push origin :<nombre_rama>  # (método antiguo)
```

---

## Sincronizar con Remoto

### Obtener cambios del remoto (sin fusionar)
```bash
git fetch origin
git fetch origin <nombre_rama>
git fetch --all  # Todas las ramas y remotes
```

### Ver cambios del remoto
```bash
git log origin/<nombre_rama>
git diff <rama_local> origin/<rama_remota>
```

### Traer cambios del remoto y fusionar
```bash
git pull origin
git pull origin <nombre_rama>
```

### Traer con rebase (en lugar de merge)
```bash
git pull --rebase origin <nombre_rama>
```

### Enviar cambios al remoto
```bash
git push origin <nombre_rama>
git push origin -u <nombre_rama>  # Primera vez (rastrear)
git push -u origin HEAD  # Rama actual
```

### Enviar todas las ramas
```bash
git push origin --all
```

### Enviar tags
```bash
git push origin --tags
```

---

## Verificar Estado

### Ver estado del repositorio
```bash
git status
git status -s  # Formato corto
```

### Ver diferencias entre archivos modificados
```bash
git diff
git diff --staged  # Cambios en staging area
git diff <rama1> <rama2>
git diff HEAD~1 HEAD  # Último commit vs commit anterior
```

### Ver historial de commits
```bash
git log
git log --oneline
git log --oneline --all --graph  # Visualización con árbol
git log --oneline -n 10  # Últimos 10 commits
git log --author="nombre"
git log --grep="palabra"
git log --follow <archivo>  # Historial de un archivo
```

### Ver un commit específico
```bash
git show <hash_commit>
git show <hash_commit>:<archivo>  # Ver archivo en ese commit
```

### Ver cambios en un archivo
```bash
git log <archivo>
git show <hash_commit>:<archivo>
```

---

## Cambios Locales

### Agregar cambios al staging area
```bash
git add <archivo>
git add .  # Todos los cambios
git add -A  # Todos (incluyendo borrados)
git add -p  # Interactivo (por partes)
```

### Quitar del staging area
```bash
git reset <archivo>
git reset  # Todos los cambios
git reset --soft HEAD~1  # Último commit sin perder cambios
git reset --hard HEAD~1  # Eliminar el último commit
```

### Hacer un commit
```bash
git commit -m "Mensaje del commit"
git commit -am "Mensaje"  # Agrega y hace commit (solo archivos modificados)
git commit --amend  # Modificar el último commit
git commit --amend --no-edit  # Agregar cambios al último commit sin cambiar mensaje
```

### Ver cambios sin commitear
```bash
git diff
git diff --cached  # Staging area
```

### Descartar cambios
```bash
git restore <archivo>
git restore .  # Todos los archivos
git checkout -- <archivo>  # (método antiguo)
```

### Guardar cambios temporalmente (stash)
```bash
git stash
git stash save "descripción"
git stash list  # Ver todos los stash
git stash pop  # Recuperar y eliminar último stash
git stash apply  # Recuperar sin eliminar
git stash apply stash@{0}  # Aplicar uno específico
git stash drop  # Eliminar un stash
git stash clear  # Eliminar todos
```

---

## Fusionar Cambios

### Fusionar una rama
```bash
git merge <nombre_rama>
```

### Fusionar y crear un commit merge
```bash
git merge --no-ff <nombre_rama>
```

### Rebase (reordenar commits)
```bash
git rebase <nombre_rama>
git rebase -i HEAD~3  # Rebase interactivo de últimos 3 commits
```

### Abortar merge o rebase
```bash
git merge --abort
git rebase --abort
```

### Continuar después de resolver conflictos
```bash
git merge --continue
git rebase --continue
```

---

## Resolver Conflictos

### Ver archivos con conflictos
```bash
git status
```

### Ver diferencia con conflictos
```bash
git diff
git diff --name-only --diff-filter=U  # Solo archivos con conflictos
```

### Usar herramienta visual (si está configurada)
```bash
git mergetool
```

### Resolver eligiendo una versión
```bash
git checkout --ours <archivo>   # Mantener tu versión
git checkout --theirs <archivo> # Mantener la otra versión
```

### Después de resolver
```bash
git add <archivo_resuelto>
git commit -m "Resolver conflictos de merge"
```

---

## Tags (Etiquetas)

### Crear un tag (anotado)
```bash
git tag -a v1.0.0 -m "Versión 1.0.0"
```

### Crear un tag (ligero)
```bash
git tag v1.0.0
```

### Listar tags
```bash
git tag
git tag -l "v1.*"
```

### Ver información de un tag
```bash
git show v1.0.0
```

### Enviar tags al remoto
```bash
git push origin v1.0.0
git push origin --tags
```

### Eliminar un tag
```bash
git tag -d v1.0.0  # Local
git push origin --delete v1.0.0  # Remoto
```

---

## Historial y Búsqueda

### Ver commits de una rama
```bash
git log <nombre_rama>
```

### Ver commits entre dos ramas
```bash
git log <rama1>..<rama2>
```

### Ver commits que no están en otra rama
```bash
git log <rama1> ^<rama2>
```

### Buscar commit por mensaje
```bash
git log --grep="palabra"
```

### Buscar cambios en archivos
```bash
git log -p <archivo>
git log --follow -p <archivo>
```

### Blame (quién y cuándo modificó cada línea)
```bash
git blame <archivo>
```

---

## Limpieza y Mantenimiento

### Limpiar archivos no rastreados (secos)
```bash
git clean -n  # Ver qué se eliminaría
git clean -fd  # Eliminar archivos y directorios
```

### Eliminar ramas locales que no tienen remoto
```bash
git branch -vv
git branch -d <rama>  # Una a una
```

### Optimizar repositorio
```bash
git gc
git gc --aggressive
```

### Ver referencias abandonadas
```bash
git reflog
```

---

## Búsqueda Avanzada

### Buscar quién introdujo un bug (bisect)
```bash
git bisect start
git bisect bad HEAD     # Commit actual es malo
git bisect good <hash>  # Este commit es bueno
# Git hace checkout automáticamente en puntos intermedios
git bisect reset  # Terminar bisect
```

### Buscar en el contenido del código
```bash
git grep "palabra"
git grep -n "palabra"  # Con número de línea
```

---

## Trabajo Colaborativo

### Ver quién trabaja en el repositorio
```bash
git shortlog -s -n  # Commits por autor
```

### Ver cambios de otros
```bash
git fetch origin
git log origin/<rama> --oneline -n 10
git diff HEAD origin/<rama>
```

### Integrar cambios remotos
```bash
git pull origin <rama>
git pull --rebase origin <rama>
```

### Crear un pull request (desde CLI)
```bash
# Necesita GitHub CLI instalado (gh)
gh pr create --base main --head sergio
```

---

## Deshacer Cambios

### Deshacer commit (crear nuevo commit que lo invierte)
```bash
git revert <hash_commit>
```

### Mover HEAD a un commit anterior
```bash
git reset --soft HEAD~1   # Guarda cambios
git reset --mixed HEAD~1  # Unstage pero guarda cambios (default)
git reset --hard HEAD~1   # Elimina cambios
```

### Recuperar commit perdido
```bash
git reflog
git reset --hard <hash>
```

### Deshacer push (si nadie más ha tirado)
```bash
git push -f origin HEAD~1:main
```

---

## Cherry-pick (Aplicar commits selectivos)

### Aplicar un commit específico
```bash
git cherry-pick <hash_commit>
```

### Cherry-pick múltiples commits
```bash
git cherry-pick <hash1> <hash2> <hash3>
git cherry-pick <hash1>..<hash2>  # Rango
```

### Abort cherry-pick
```bash
git cherry-pick --abort
```

---

## Configuración Avanzada

### Alias útiles
```bash
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.unstage 'reset HEAD --'
git config --global alias.last 'log -1 HEAD'
git config --global alias.visual 'log --graph --oneline --all'
```

### Ver configuración de remoto
```bash
git remote -v
git remote show origin
```

### Cambiar URL del remoto
```bash
git remote set-url origin <nueva_URL>
```

### Agregar segundo remoto
```bash
git remote add upstream <URL>
git fetch upstream
```

---

## Flujo de Trabajo Git Recomendado (GitHub Flow)

1. **Crear rama de feature**
   ```bash
   git switch -c feature/nueva-funcionalidad
   ```

2. **Hacer cambios y commits**
   ```bash
   git add .
   git commit -m "Agregar nueva funcionalidad"
   ```

3. **Empujar la rama**
   ```bash
   git push -u origin feature/nueva-funcionalidad
   ```

4. **Crear Pull Request en GitHub**

5. **Después de merge, limpiar**
   ```bash
   git switch main
   git pull origin main
   git branch -d feature/nueva-funcionalidad
   ```

---

## Comandos Útiles para Tu Proyecto

### Ver todas las ramas en el proyecto
```bash
git branch -a
```

### Cambiar entre tus ramas (sergio, eric, nicold, pruebas, main)
```bash
git switch sergio
git switch eric
git switch nicold
git switch pruebas
git switch main
```

### Actualizar tu rama con cambios de main
```bash
git switch sergio
git fetch origin
git rebase origin/main
```

### Ver diferencias entre tu rama y main
```bash
git diff main..sergio
```

### Crear PR desde línea de comandos (requiere GitHub CLI)
```bash
gh auth login  # Primera vez
gh pr create --base main --head sergio
```

---

## Tips y Mejores Prácticas

✅ **Hacer commit frecuentemente** - Con mensajes descriptivos  
✅ **Sincronizar con remoto** - `git pull` antes de empezar a trabajar  
✅ **Crear ramas para features** - No trabajar en main directamente  
✅ **Escribir mensajes claros** - Facilita entender el historial  
✅ **Revisar cambios antes de commit** - Con `git diff` y `git status`  
✅ **Usar `.gitignore`** - Para no commitear archivos innecesarios  

⚠️ **Evitar** - `git push -f` en ramas compartidas  
⚠️ **Evitar** - Commits muy grandes sin relacionar  
⚠️ **Evitar** - Trabajar directamente en main/master  

---

## Recursos Adicionales

- [Documentación oficial Git](https://git-scm.com/doc)
- [GitHub Docs](https://docs.github.com)
- [Interactive Git Tutorial](https://learngitbranching.js.org/)
- [Git Cheat Sheet](https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf)
