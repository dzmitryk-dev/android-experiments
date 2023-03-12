import os
import subprocess

def find_all_projects_dirs():
    dirs = [f for f in os.listdir() if os.path.isdir(f)]
    projects_dirs = [d for d in dirs if os.path.exists(os.path.join(d, "gradlew")) or os.path.exists(os.path.join(d, "gradlew.bat"))]
    print("Projects dirs: \n" + str(projects_dirs))
    return projects_dirs

def build_project(d):
    print("Build " + d)
    subprocess.call("./gradlew build", cwd=d, shell=True)