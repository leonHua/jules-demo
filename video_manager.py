#!/usr/bin/env python3
import os
import sys
import asyncio
import pathlib
import subprocess
from typing import List, Dict, Any, Optional
from moviepy.editor import VideoFileClip
from nicegui import ui

# --- UI Constants & Theme ---
# Enable dark mode for a more modern look
ui.dark_mode().enable()

RESOLUTION_OPTIONS = {
    'All': None,
    '480p (<= 480h)': 480,
    '720p (<= 720h)': 720,
    '1080p (<= 1080h)': 1080,
    '2K (<= 1440h)': 1440,
    '4K (<= 2160h)': 2160,
}
FILE_TYPE_OPTIONS = ['all', 'mp4', 'avi', 'mkv', 'mov', 'wmv', 'flv']

AG_GRID_COLUMN_DEFS = [
    {'headerName': 'ID', 'field': 'id', 'width': 90, 'checkboxSelection': True, 'headerCheckboxSelection': True},
    {'headerName': 'File Path', 'field': 'path', 'width': 500, 'sortable': True, 'filter': True},
    {'headerName': 'Resolution', 'field': 'resolution', 'width': 150, 'sortable': True},
    {'headerName': 'File Size', 'field': 'size_mb', 'width': 150, 'sortable': True},
]

# --- System & File Operations ---
def open_file(path: str):
    """Opens a file with the default system application in a cross-platform way."""
    try:
        if sys.platform == "win32":
            os.startfile(path)
        elif sys.platform == "darwin":
            subprocess.run(["open", path], check=True)
        else:
            subprocess.run(["xdg-open", path], check=True)
    except Exception as e:
        ui.notify(f"Failed to open file: {e}", color='negative')
        print(f"Error opening file {path}: {e}")

async def scan_videos(path: str, max_height: Optional[int], file_types: List[str]) -> List[Dict[str, Any]]:
    """Scans a directory for video files and returns their metadata asynchronously."""
    base_path = pathlib.Path(path)
    if not base_path.is_dir():
        return []

    patterns = ['*.' + ext for ext in FILE_TYPE_OPTIONS if ext != 'all'] if 'all' in file_types else ['*.' + ext for ext in file_types]

    files_to_process = sorted(list(set(p for pattern in patterns for p in base_path.rglob(pattern) if p.is_file())))

    def process_file(p: pathlib.Path) -> Optional[Dict[str, Any]]:
        try:
            size_bytes = p.stat().st_size
            with VideoFileClip(str(p)) as clip:
                width, height = clip.size
            if max_height is not None and height > max_height:
                return None
            return {
                'path': str(p),
                'resolution': f"{width}x{height}",
                'size_mb': f"{size_bytes / (1024*1024):.2f} MB",
            }
        except Exception as e:
            print(f"Could not process file {p}: {e}")
            return None

    tasks = [asyncio.to_thread(process_file, p) for p in files_to_process]
    results = await asyncio.gather(*tasks)

    # Filter out None results and assign sequential IDs
    video_files = [res for res in results if res is not None]
    for i, video in enumerate(video_files):
        video['id'] = i + 1

    return video_files

# --- Main UI Page ---
@ui.page('/')
def main_page():
    ui.add_head_html('<style>.nicegui-content { padding: 1.5rem; }</style>')
    ui.label('🎬 Advanced Video File Manager').classes('text-h4 text-weight-bold q-mb-lg')

    with ui.card().classes('w-full').props('flat bordered'):
        with ui.row().classes('w-full items-center gap-4'):
            path_input = ui.input(
                label='Directory Path',
                value=os.path.expanduser("~"),
            ).classes('flex-grow').props('clearable outlined dense')

            resolution_select = ui.select(
                label='Max Resolution', options=RESOLUTION_OPTIONS, value='All'
            ).classes('w-48').props('outlined dense')

            file_type_select = ui.select(
                label='File Type', options=FILE_TYPE_OPTIONS, value='all'
            ).classes('w-32').props('outlined dense')

        with ui.row().classes('w-full justify-start items-center gap-2 q-mt-md'):
            scan_button = ui.button('Scan Directory', icon='search')
            delete_button = ui.button('Delete Selected', icon='delete', color='red')

    grid = ui.aggrid({
        'columnDefs': AG_GRID_COLUMN_DEFS,
        'rowData': [],
        'rowSelection': 'multiple',
        'suppressRowClickSelection': True,
        'domLayout': 'autoHeight',
        'defaultColDef': {'flex': 1},
        'overlayNoRowsTemplate': '<span class="text-gray-500">No videos found. Please scan a directory.</span>',
    }).classes('w-full mt-4')

    async def scan_files_handler():
        path = path_input.value
        if not (path and os.path.isdir(path)):
            ui.notify(f"Error: Directory not found at '{path}'", color='negative')
            return

        max_height = resolution_select.value
        file_types = [file_type_select.value]

        try:
            scan_button.disable()
            delete_button.disable()
            grid.call_api_method('showLoadingOverlay')
            video_data = await scan_videos(path, max_height, file_types)
            grid.options['rowData'] = video_data
            await grid.update()
            ui.notify(f"Scan complete. Found {len(video_data)} videos.", color='positive')
        except Exception as e:
            ui.notify(f"An unexpected error occurred: {e}", color='negative')
        finally:
            grid.call_api_method('hideOverlay')
            scan_button.enable()
            delete_button.enable()

    async def delete_files_handler():
        selected_rows = await grid.get_selected_rows()
        if not selected_rows:
            ui.notify("Please select files to delete.", color='warning')
            return

        with ui.dialog() as dialog, ui.card():
            ui.label(f"Permanently delete {len(selected_rows)} selected file(s)? This cannot be undone.")
            with ui.row().classes('w-full justify-end mt-4'):
                ui.button('Cancel', on_click=dialog.close)
                ui.button('Delete', on_click=lambda: dialog.submit('delete'), color='red')

        if await dialog == 'delete':
            paths_to_delete = {row['path'] for row in selected_rows}
            deleted_count = 0
            for path in paths_to_delete:
                try:
                    os.remove(path)
                    deleted_count += 1
                except OSError as e:
                    ui.notify(f"Error deleting {os.path.basename(path)}", color='negative')

            if deleted_count > 0:
                ui.notify(f"Successfully deleted {deleted_count} file(s).", color='positive')
                grid.options['rowData'] = [row for row in grid.options['rowData'] if row['path'] not in paths_to_delete]
                await grid.update()

    def play_video_handler(e):
        open_file(e.args['data']['path'])

    # Bind event handlers
    scan_button.on('click', scan_files_handler)
    delete_button.on('click', delete_files_handler)
    grid.on('cellDoubleClicked', play_video_handler)

# --- Run the App ---
ui.run()