import common

projects_dirs = common.find_all_projects_dirs()
for p in projects_dirs:
    common.build_project(p)
    